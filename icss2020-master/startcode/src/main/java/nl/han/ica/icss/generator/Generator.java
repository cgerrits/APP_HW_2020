package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

import java.util.ArrayList;

public class Generator {
	private int spaceCounter = 0;
	private ArrayList<ASTNode> variableAssignments = null;

	public String generate(AST ast) {
		variableAssignments = getVariableAssignmentsFromAst(ast);
		ASTNode currentNode = ast.root;
		StringBuilder builder = new StringBuilder();
		// get children of the stylesheet
		ArrayList<ASTNode> traversables = currentNode.getChildren();
		for (ASTNode traversable : traversables) {
			traverse(builder, traversable);
		}
		return builder.toString();
	}

	private void traverse(StringBuilder builder, ASTNode node) {
		if (node instanceof Stylerule) {
			traverseStyleRule(builder, node);
		}
	}


	private void traverseStyleRule(StringBuilder builder, ASTNode node) {
		spaceCounter = 0;
		Stylerule stylerule = (Stylerule) node;
		traverseSelectors(builder, stylerule.selectors.get(0));
		for (ASTNode child : stylerule.body) {
			if(child instanceof  Declaration) {
				traverseDeclaration(builder, child);
			}
		}
		spaceCounter = 0;
		builder.append("}" + "\n" + "\n");

	}

	private void traverseDeclaration(StringBuilder builder, ASTNode node) {
		// spaceCounter is al correct gezet na de selector;
		ArrayList<ASTNode> children = node.getChildren();
		// 1 is een variableassignment en wil ik natuurlijk niet zien in de css compliant string
		if (children.size() == 2) {
			addCorrectAmountOfSpaces(builder);
			if(!(children.get(0)instanceof VariableReference)) {
				PropertyName propertyName = (PropertyName) children.get(0);
				builder.append(propertyName.name + ": ");
				traversePropertyLiteralType(builder, children.get(1));
			}
		}
	}

	// later nog rekening houden met variabele declaraties en multiply en add
	private void traversePropertyLiteralType(StringBuilder builder, ASTNode node) {
		if (node instanceof ColorLiteral) {
			ColorLiteral literal = (ColorLiteral) node;
			builder.append(literal.value + ";" + "\n");
		} else if (node instanceof PercentageLiteral) {
			PercentageLiteral literal = (PercentageLiteral) node;
			System.out.println("literal value:" + literal.value);
			builder.append(literal.value + "%;" + "\n");

		} else if (node instanceof PixelLiteral) {
			PixelLiteral literal = (PixelLiteral) node;
			System.out.println("literal value:" + literal.value);
			builder.append(literal.value + "px;" + "\n");
		} else if (node instanceof VariableReference) {
			VariableReference literal = (VariableReference) node;
			System.out.println(node);
			VariableAssignment variableAssignment = getAssignment(literal);
			traversePropertyLiteralType(builder, variableAssignment.expression);
		}
	}


	private void traverseSelectors(StringBuilder builder, ASTNode node) {
		if (node instanceof TagSelector) {
			TagSelector nodeSelector = ((TagSelector) node);
			builder.append(nodeSelector.tag + " {" + "\n");
			spaceCounter = 2;
		} else if (node instanceof IdSelector) {
			IdSelector nodeSelector = ((IdSelector) node);
			builder.append(nodeSelector.id + " {" + "\n");
			spaceCounter = 2;
		} else {
			ClassSelector nodeSelector = ((ClassSelector) node);
			builder.append(nodeSelector.cls + " {" + "\n");
			spaceCounter = 2;
		}
	}

	private void addCorrectAmountOfSpaces(StringBuilder builder) {
		for (int i = 0; i < spaceCounter; i++) {
			builder.append(" ");
		}
	}
// helpers

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
}
