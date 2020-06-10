package nl.han.ica.icss.checker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.errorprone.annotations.Var;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.*;

public class Checker {

    private LinkedList<HashMap<String,ExpressionType>> variableTypes;
    private ArrayList<ASTNode> variableAssignments = null;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        checkIfVariablesCorrectlyInserted(ast);
        variableAssignments = getVariableAssignmentsFromAst(ast);
        checkForColorsInOperations(ast);
        checkIfClauseExpressions(ast);
    }

    //controleert expressies van if statements
    private void checkIfClauseExpressions(AST ast){

        ArrayList<ASTNode> ifs = getAllIfClause(ast);
        for (ASTNode ifClause: ifs) {
            if(!checkIfExpressionsBoolean((IfClause) ifClause)){
                ifClause.setError("expression needs to be a boolean");
            }
        }
    }

    private boolean checkIfExpressionsBoolean(IfClause ifClause){
        Expression expression = ifClause.conditionalExpression;
        if(expression instanceof  BoolLiteral){
            return true;
        }
        else if(expression instanceof VariableReference){
            VariableReference varref = (VariableReference) expression;
            VariableAssignment finalAssignment = getAssignment(varref);
            if(finalAssignment.expression instanceof BoolLiteral){
                return true;
            }

        }
        return false;
    }

    // haalt de bovenste VariableAssignment van een reference door recursief door alle references te gaan tot ie een assignment tegen komt waarvan de expression gaan variablereference is
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


    // color literal kan sowieso niet in een som omdat de grammatica dat niet toestaat.
    private void checkForColorsInOperations(AST ast){
        ArrayList<ASTNode> operations = getOperations(ast);
        for (ASTNode operation: operations) {
            ArrayList<ASTNode> listOfChildren = operation.getChildren();
            for (ASTNode child: listOfChildren) {
                if(child instanceof ColorLiteral){
                    operation.setError("This operations contains a color and that's a nono");
                }
                else if(child instanceof  VariableReference) {
                    if (checkIfVariableAlreadyDefined((VariableReference) child, variableAssignments)) {
                        if (getAssignment((VariableReference) child).expression instanceof ColorLiteral) {
                            operation.setError("Color found in variable that was used in an operation");
                        }
                    }
                }
            }
        }
    }

    private ArrayList<ASTNode> getOperations(AST ast){
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

    // variable references uit de AST structuur halen om te kijken of er references zijn zonder assignments.
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

    private void checkIfVariablesCorrectlyInserted(AST ast){
        ArrayList<ASTNode> variableAssignments = getVariableAssignmentsFromAst(ast);
        ArrayList<ASTNode> variableReferences = getVariableReferencesFromAst(ast);
        for (ASTNode node: variableReferences) {
            checkIfVariableAlreadyDefined((VariableReference) node, variableAssignments);
        }

    }

    // kijkt of de reference in de variable assignments voorkomt.
    private boolean checkIfVariableAlreadyDefined(VariableReference reference, ArrayList<ASTNode> variableAssignments){
        for (ASTNode assignment: variableAssignments) {
            VariableAssignment variableAssignment = (VariableAssignment) assignment;
            if(((VariableAssignment) assignment).name.name.equals(reference.name)){
                return true;
            }
        }
        reference.setError("variable has not been defined: " + reference.name);
        return false;

    }


    // variable references uit de AST structuur halen om te kijken of er references zijn zonder assignments.
    private ArrayList<ASTNode> getVariableReferencesFromAst(AST ast){
        ArrayList<ASTNode> getVariableReferences = getAllVariableReferences(new ArrayList<>(),ast.root);
        return getVariableReferences;
    }

    private ArrayList<ASTNode> getAllVariableReferences(ArrayList<ASTNode> listOfCurrentNodes, ASTNode currentNode){
        if(currentNode.getChildren().size() > 0){
            for (ASTNode child :currentNode.getChildren()) {
                if(child instanceof  VariableReference){
                    listOfCurrentNodes.add(child);
                }
                getAllVariableReferences(listOfCurrentNodes, child);
            }
        }
        return listOfCurrentNodes;
    }
}