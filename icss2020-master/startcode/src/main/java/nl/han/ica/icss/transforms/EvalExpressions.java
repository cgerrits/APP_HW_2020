package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class EvalExpressions implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;

    public EvalExpressions() {
        variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        ResolveVariables resolveVariables = new ResolveVariables();
        // verandert alle variabelereferenties naar de juiste literals
        resolveVariables.apply(ast);
        variableValues = new LinkedList<>();
        ArrayList<ASTNode> operations = getAllOperations(ast);
        // gaat achteruit zodat ze onderaan eerst pakt en die eerst verandert naar literals
        for (int i = operations.size() -1; i >= 0; i--) {
            handleOperation(ast, operations.get(i));
        }

    }

    // functie die de parent aanpast en de literal invoegt op de juiste plaats nadat de operatie klaar is
    private void changeParent(AST ast, ASTNode parent, ASTNode solved, ASTNode operation){
        if(solved instanceof PixelLiteral){
            if(parent instanceof Declaration) {
                Declaration parentDeclaration = (Declaration) parent;
                parentDeclaration.expression = (PixelLiteral) solved;
            }
            else if(parent instanceof Operation){
                Operation parentOperation = (Operation) parent;

                if(parentOperation.lhs.equals(operation)){
                    parentOperation.lhs = (PixelLiteral) solved;
                }
                else {
                    parentOperation.rhs = (PixelLiteral) solved;
                }
            }
        }
        else if(solved instanceof PercentageLiteral){
            if(parent instanceof Declaration) {
                Declaration parentDeclaration = (Declaration) parent;
                parentDeclaration.expression = (PercentageLiteral) solved;
            }
            else if(parent instanceof Operation){
                Operation parentOperation = (Operation) parent;

                if(parentOperation.lhs.equals(operation)){
                    parentOperation.lhs = (PercentageLiteral) solved;
                }
                else {
                    parentOperation.rhs = (PercentageLiteral) solved;
                }
            }
        }

    }


    //functie die de kijkt welk type operation het is en vervolgens de juiste functie aanroept om deze op te lossen
    private void handleOperation(AST ast, ASTNode operation) {
        if(operation instanceof MultiplyOperation){
            ASTNode parent = getParent(ast, (MultiplyOperation) operation);
            MultiplyOperation operatie = (MultiplyOperation) operation;
            ASTNode solved = multiplyOperationSolver(operatie);
            changeParent(ast,parent,solved,operation);

        }
        else if(operation instanceof AddOperation){
            ASTNode parent = getParent(ast, (AddOperation) operation);
            AddOperation operatie = (AddOperation) operation;
            ASTNode solved = addOperationSolver(operatie);
            changeParent(ast,parent,solved,operation);
        }
        else if(operation instanceof SubtractOperation){
            ASTNode parent = getParent(ast, (SubtractOperation) operation);
            SubtractOperation operatie = (SubtractOperation) operation;
            ASTNode solved = subtractSolver(operatie);
            changeParent(ast,parent,solved,operation);
        }
    }
    // functie die een node retourneer met de literal die uit de operation komt.
    private ASTNode multiplyOperationSolver(MultiplyOperation operation){
        if(operation.lhs instanceof PixelLiteral && operation.rhs instanceof ScalarLiteral){
            PixelLiteral left = (PixelLiteral)operation.lhs;
            ScalarLiteral right = (ScalarLiteral) operation.rhs;
            return new PixelLiteral(left.value * right.value);
        }
        else if(operation.lhs instanceof ScalarLiteral && operation.rhs instanceof PixelLiteral ){
            ScalarLiteral left = (ScalarLiteral)operation.lhs;
            PixelLiteral right = (PixelLiteral) operation.rhs;
            return new PixelLiteral(left.value * right.value);
        }
        else if(operation.lhs instanceof PercentageLiteral && operation.rhs instanceof ScalarLiteral){
            PercentageLiteral left = (PercentageLiteral) operation.lhs;
            ScalarLiteral right = (ScalarLiteral) operation.rhs;
            return new PercentageLiteral(left.value * right.value);
        }
        else if(operation.lhs instanceof ScalarLiteral && operation.rhs instanceof PercentageLiteral){
            ScalarLiteral left = (ScalarLiteral) operation.lhs;
            PercentageLiteral right = (PercentageLiteral) operation.rhs;
            return new PercentageLiteral(left.value * right.value);
        }

        else return null;
    }


    //functie die een node retourneer met de literal die uit de operation komt.
    private ASTNode addOperationSolver(AddOperation operation){
        if(operation.lhs instanceof PixelLiteral && operation.rhs instanceof PixelLiteral){
            PixelLiteral left = (PixelLiteral)operation.lhs;
            PixelLiteral right = (PixelLiteral)operation.rhs;
            return new PixelLiteral(left.value + right.value);
        }
        else if(operation.lhs instanceof PixelLiteral && operation.rhs instanceof ScalarLiteral){
            PixelLiteral left = (PixelLiteral)operation.lhs;
            ScalarLiteral right = (ScalarLiteral) operation.rhs;
            return new PixelLiteral(left.value + right.value);
        }
        else if(operation.lhs instanceof ScalarLiteral && operation.rhs instanceof PixelLiteral ){
            ScalarLiteral left = (ScalarLiteral)operation.lhs;
            PixelLiteral right = (PixelLiteral) operation.rhs;
            return new PixelLiteral(left.value + right.value);
        }
        else if(operation.lhs instanceof PercentageLiteral && operation.rhs instanceof PercentageLiteral){
            PercentageLiteral left = (PercentageLiteral) operation.lhs;
            PercentageLiteral right = (PercentageLiteral) operation.rhs;
            return new PercentageLiteral(left.value + right.value);
        }
        else if(operation.lhs instanceof PercentageLiteral && operation.rhs instanceof ScalarLiteral){
            PercentageLiteral left = (PercentageLiteral) operation.lhs;
            ScalarLiteral right = (ScalarLiteral) operation.rhs;
            return new PercentageLiteral(left.value + right.value);
        }
        else if(operation.lhs instanceof ScalarLiteral && operation.rhs instanceof PercentageLiteral){
            ScalarLiteral left = (ScalarLiteral) operation.lhs;
            PercentageLiteral right = (PercentageLiteral) operation.rhs;
            return new PercentageLiteral(left.value + right.value);
        }
        else return null;
    }


    // functie die een node retourneer met de literal die uit de operation komt.
    private ASTNode subtractSolver(SubtractOperation operation){
        if(operation.lhs instanceof PixelLiteral && operation.rhs instanceof PixelLiteral){
            PixelLiteral left = (PixelLiteral)operation.lhs;
            PixelLiteral right = (PixelLiteral)operation.rhs;
            return new PixelLiteral(left.value - right.value);
        }
        else if(operation.lhs instanceof PixelLiteral && operation.rhs instanceof ScalarLiteral){
            PixelLiteral left = (PixelLiteral)operation.lhs;
            ScalarLiteral right = (ScalarLiteral) operation.rhs;
            return new PixelLiteral(left.value - right.value);
        }
        else if(operation.lhs instanceof ScalarLiteral && operation.rhs instanceof PixelLiteral ){
            ScalarLiteral left = (ScalarLiteral)operation.lhs;
            PixelLiteral right = (PixelLiteral) operation.rhs;
            return new PixelLiteral(left.value - right.value);
        }
        else if(operation.lhs instanceof PercentageLiteral && operation.rhs instanceof PercentageLiteral){
            PercentageLiteral left = (PercentageLiteral) operation.lhs;
            PercentageLiteral right = (PercentageLiteral) operation.rhs;
            return new PercentageLiteral(left.value - right.value);
        }
        else if(operation.lhs instanceof PercentageLiteral && operation.rhs instanceof ScalarLiteral){
            PercentageLiteral left = (PercentageLiteral) operation.lhs;
            ScalarLiteral right = (ScalarLiteral) operation.rhs;
            return new PercentageLiteral(left.value - right.value);
        }
        else if(operation.lhs instanceof ScalarLiteral && operation.rhs instanceof PercentageLiteral){
            ScalarLiteral left = (ScalarLiteral) operation.lhs;
            PercentageLiteral right = (PercentageLiteral) operation.rhs;
            return new PercentageLiteral(left.value - right.value);
        }
        else return null;
    }

    private ASTNode getParent(AST ast, Operation operation){
        return getParent(ast.root, operation);
    }
    //tree traversel om parent te zoeken
    private ASTNode getParent(ASTNode currentNode, Operation operation){
        ArrayList<ASTNode> childrenOfCurrentNode = currentNode.getChildren();
        for (ASTNode node:childrenOfCurrentNode) {
            if(node.equals(operation)){
                return currentNode;
            }
            else{
                ASTNode parent = getParent(node, operation);
                if(parent != null){
                    return parent;
                }
            }
        }
        return null;
    }


    private ArrayList<ASTNode> getAllOperations(AST ast){
        return getAllOperations(new ArrayList<>(), ast.root);
    }

    private ArrayList<ASTNode> getAllOperations(ArrayList<ASTNode> listOfCurrentNodes, ASTNode currentNode){
        if(currentNode.getChildren().size() > 0){
            for (ASTNode child :currentNode.getChildren()) {
                if(child instanceof Operation){
                    listOfCurrentNodes.add(child);
                }
                getAllOperations(listOfCurrentNodes, child);
            }
        }
        return listOfCurrentNodes;
    }

}
