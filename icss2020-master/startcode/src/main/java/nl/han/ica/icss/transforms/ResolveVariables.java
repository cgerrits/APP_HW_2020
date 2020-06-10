package nl.han.ica.icss.transforms;

import com.google.errorprone.annotations.Var;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.LinkedList;

// doel is om alle Variabele die geen boolliteral hebben als type al op te lossen zodat de eval niet te maken kan hebben met variableReferences
public class ResolveVariables implements Transform{

    ArrayList<ASTNode>  variableAssignments = null;
    AST bleh = null;

    @Override
    public void apply(AST ast) {
        bleh =ast;
        variableAssignments = getVariableAssignmentsFromAst(ast);
        ArrayList<ASTNode> references = getVariableReferencesFromAst(ast);
        for (ASTNode reference: references) {
            handleReference(ast, (VariableReference) reference);

        }
    }

    // functie die kijkt naar welk type de referentie is en past zijn parent aan en vervangt het met de juiste literal
    private void handleReference(AST ast, VariableReference reference) {
        VariableReference castReference = reference;
        ASTNode parent = getParent(ast,castReference);
        VariableAssignment topLevelAssignment = getAssignment(reference);
        if(parent instanceof Declaration){
            ((Declaration) parent).expression = topLevelAssignment.expression;
        }
        else if(parent instanceof MultiplyOperation){
            MultiplyOperation castParent = ((MultiplyOperation) parent);
            if(castParent.lhs.equals(castReference)){
                castParent.lhs = topLevelAssignment.expression;
            }
            else {
                castParent.rhs = topLevelAssignment.expression;
            }
        }
        else if(parent instanceof AddOperation){
            AddOperation castParent = ((AddOperation) parent);
            if(castParent.lhs.equals(castReference)){
                castParent.lhs = topLevelAssignment.expression;
            }
            else {
                castParent.rhs = topLevelAssignment.expression;
            }
        }
        else if(parent instanceof SubtractOperation){
            SubtractOperation castParent = ((SubtractOperation) parent);
            if(castParent.lhs.equals(castReference)){
                castParent.lhs = topLevelAssignment.expression;
            }
            else {
                castParent.rhs = topLevelAssignment.expression;
            }
        }
    }


    private VariableAssignment getAssignment(VariableReference variableReference) {
        ArrayList<ASTNode> assignments = variableAssignments;
        VariableAssignment assigned = null;
        for (ASTNode assignment : assignments) {
            VariableAssignment cast = (VariableAssignment) assignment;
            if (cast.name.name.equals(variableReference.name)) {
                assigned = cast;
                if (assigned.expression instanceof VariableReference) {
                    VariableReference reference = (VariableReference) assigned.expression;
                    VariableAssignment variableAssignment = getAssignment(reference);
                    if (variableAssignment != null) {
                        return variableAssignment;
                    }
                }
            }
        }
        return assigned;
    }

    private ArrayList<ASTNode> getVariableAssignmentsFromAst(AST ast) {
        return getAllVariableAssignments(new ArrayList<>(), ast.root);
    }

    private ArrayList<ASTNode> getAllVariableAssignments(ArrayList<ASTNode> listOfCurrentNodes, ASTNode currentNode) {
        if (currentNode.getChildren().size() > 0) {
            for (ASTNode child : currentNode.getChildren()) {
                if (child instanceof VariableAssignment) {
                    listOfCurrentNodes.add(child);
                }
                getAllVariableAssignments(listOfCurrentNodes, child);
            }
        }
        return listOfCurrentNodes;
    }

    private ArrayList<ASTNode> getVariableReferencesFromAst(AST ast) {
        return getAllVariableReferences(new ArrayList<>(), ast.root);
    }

    private ArrayList<ASTNode> getAllVariableReferences(ArrayList<ASTNode> listOfCurrentNodes, ASTNode currentNode) {
        if (currentNode.getChildren().size() > 0) {
            for (ASTNode child : currentNode.getChildren()) {
                if (child instanceof VariableReference) {
                    listOfCurrentNodes.add(child);
                }
                getAllVariableReferences(listOfCurrentNodes, child);
            }
        }
        return listOfCurrentNodes;
    }

    private ASTNode getParent(AST ast, VariableReference clause){
        return getParent(ast.root, clause);
    }
    //tree traversel om parent te zoeken
    private ASTNode getParent(ASTNode currentNode, VariableReference clause){
        ArrayList<ASTNode> childrenOfCurrentNode = currentNode.getChildren();
        for (ASTNode node:childrenOfCurrentNode) {
            if(node == clause){
                return currentNode;
            }
            else{
                ASTNode parent = getParent(node, clause);
                if(parent != null){
                    return parent;
                }
            }
        }
        return null;
    }
}
