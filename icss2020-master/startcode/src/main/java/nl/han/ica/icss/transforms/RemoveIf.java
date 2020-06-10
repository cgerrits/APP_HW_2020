package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.sql.Array;
import java.util.ArrayList;

public class RemoveIf implements Transform {

    @Override
    public void apply(AST ast) {
        removeIfs(ast);
    }

    private void removeIfs(AST ast){
        ArrayList<ASTNode> ifclauses = getAllIfClause(ast);
        for (ASTNode ifclause: ifclauses) {
            ASTNode parent = getParent(ast,(IfClause) ifclause);
            IfClause ifClause = (IfClause) ifclause;
            if(ifClause.conditionalExpression instanceof BoolLiteral){
                BoolLiteral bool = (BoolLiteral) ifClause.conditionalExpression;
                if(bool.value){
                    ArrayList<ASTNode> ifChildren = ((IfClause) ifclause).body;
                    for (ASTNode child: ifChildren) {
                        parent.addChild(child);
                    }
                    parent.removeChild(ifClause);
                }
                else{
                    parent.removeChild(ifClause);
                }
            }
            else if(ifClause.conditionalExpression instanceof VariableReference){
                VariableReference ref = (VariableReference) ifClause.conditionalExpression;
                VariableAssignment finalAssignment = getAssignment(ref, ast);
                BoolLiteral boolLiteral = (BoolLiteral) finalAssignment.expression;
                if(boolLiteral.value){
                    ArrayList<ASTNode> ifChildren = ((IfClause) ifclause).body;
                    for (ASTNode child: ifChildren) {
                        parent.addChild(child);
                    }
                    parent.removeChild(ifClause);
                }
                else{
                    parent.removeChild(ifClause);
                }
            }
        }
    }


    // deze functie en de helper halen alle if clauses en doen ze in een arraylist
    private ArrayList<ASTNode> getAllIfClause(AST ast){
        return getAllIfClause(new ArrayList<>(), ast.root);
    }

    private ArrayList<ASTNode> getAllIfClause(ArrayList<ASTNode> listOfCurrentNodes, ASTNode currentNode){
        if(currentNode.getChildren().size() > 0){
            for (ASTNode child :currentNode.getChildren()) {
                if(child instanceof IfClause){
                    listOfCurrentNodes.add(child);
                }
                getAllIfClause(listOfCurrentNodes, child);
            }
        }
        return listOfCurrentNodes;
    }


    private ASTNode getParent(AST ast, IfClause clause){
        return getParent(ast.root, clause);
    }
    //tree traversel om parent te zoeken
    private ASTNode getParent(ASTNode currentNode, IfClause clause){
        ArrayList<ASTNode> childrenOfCurrentNode = currentNode.getChildren();
        for (ASTNode node:childrenOfCurrentNode) {
            if(node.equals(clause)){
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

    // recursieve functie die de assignment zoekt die geen referentie is en daar de type literal uit haalt.
    private VariableAssignment getAssignment(VariableReference variableReference, AST ast){
        ArrayList<ASTNode> variableAssignments = getVariableAssignmentsFromAst(ast);
        VariableAssignment assigned = null;
        for (ASTNode assignment: variableAssignments) {
            VariableAssignment cast = (VariableAssignment) assignment;
            if(cast.name.name.equals(variableReference.name)){
                assigned = cast;
                if(assigned.expression instanceof  VariableReference){
                    VariableReference reference = (VariableReference) assigned.expression;
                    VariableAssignment variableAssignment = getAssignment(reference, ast);
                    if(variableAssignment != null){
                        return variableAssignment;
                    }
                }
            }
        }
        return assigned;
    }

    private ArrayList<ASTNode> getVariableAssignmentsFromAst(AST ast){
        return getAllVariableAssignments(new ArrayList<>(),ast.root);
    }

    private ArrayList<ASTNode> getAllVariableAssignments(ArrayList<ASTNode> listOfCurrentNodes, ASTNode currentNode){
        if(currentNode.getChildren().size() > 0){
            for (ASTNode child :currentNode.getChildren()) {
                if(child instanceof  VariableAssignment){
                    listOfCurrentNodes.add(child);
                }
                getAllVariableAssignments(listOfCurrentNodes, child);
            }
        }
        return listOfCurrentNodes;
    }

}
