package nl.han.ica.icss.parser;

import java.util.Stack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private Stack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new Stack<>();
	}
	public AST getAST() {
		return ast;
	}

	@Override public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		currentContainer.push(new Stylesheet());
	}



	@Override public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		ast.root = (Stylesheet) currentContainer.pop();
	}

	@Override public void enterStylerule(ICSSParser.StyleruleContext ctx) {
		currentContainer.push(new Stylerule());

	}

	@Override public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}


	@Override public void enterClassselector(ICSSParser.ClassselectorContext ctx) {
		currentContainer.push(new ClassSelector(ctx.getText()));
	}

	@Override public void exitClassselector(ICSSParser.ClassselectorContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterIdselector(ICSSParser.IdselectorContext ctx) {
		currentContainer.push(new IdSelector(ctx.getText()));
	}

	@Override public void exitIdselector(ICSSParser.IdselectorContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterTagselector(ICSSParser.TagselectorContext ctx) {
		currentContainer.push(new TagSelector(ctx.getText()));}

	@Override public void exitTagselector(ICSSParser.TagselectorContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	//selector is an abstract class, didnt respond as i expected. fix later and maybe remove it from the grammar if i dont need it.

	@Override public void enterSelector(ICSSParser.SelectorContext ctx) {
	}

	@Override public void exitSelector(ICSSParser.SelectorContext ctx) { }

	@Override public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		String[] splitStrings = ctx.getText().split(":");
		currentContainer.push(new Declaration(splitStrings[0]));
	}

	@Override public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}


	@Override public void enterVariableassignment(ICSSParser.VariableassignmentContext ctx) {
		String[] splitStrings = ctx.getText().split(":=");

		currentContainer.push(new VariableAssignment());
		currentContainer.peek().addChild(new VariableReference(splitStrings[0]));
	}

	@Override public void exitVariableassignment(ICSSParser.VariableassignmentContext ctx) {
		VariableAssignment variableAssignment = (VariableAssignment) currentContainer.peek();
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterVariablereference(ICSSParser.VariablereferenceContext ctx) {

		currentContainer.push(new VariableReference(ctx.getText()));
	}

	@Override public void exitVariablereference(ICSSParser.VariablereferenceContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterBoolliteral(ICSSParser.BoolliteralContext ctx) {
		if(ctx.getText().equals("TRUE")) {
			currentContainer.push((new BoolLiteral(true)));
		}
		else{
			currentContainer.push((new BoolLiteral(false)));
		}
	}

	@Override public void exitBoolliteral(ICSSParser.BoolliteralContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterPixelliteral(ICSSParser.PixelliteralContext ctx) {
		currentContainer.push(new PixelLiteral(ctx.getText()));
	}

	@Override public void exitPixelliteral(ICSSParser.PixelliteralContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterPercentageliteral(ICSSParser.PercentageliteralContext ctx) {

		currentContainer.push(new PercentageLiteral(ctx.getText()));
	}
	@Override public void exitPercentageliteral(ICSSParser.PercentageliteralContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}
	@Override public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		currentContainer.push(new ColorLiteral(ctx.getText()));
	}
	@Override public void exitColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
		currentContainer.push(new ScalarLiteral(ctx.getText()));
	}
	@Override public void exitScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}


	@Override public void enterMultiplyoperation(ICSSParser.MultiplyoperationContext ctx) {
		MultiplyOperation multiplyOperation = new MultiplyOperation();
		currentContainer.push(multiplyOperation);
	}

	@Override public void exitMultiplyoperation(ICSSParser.MultiplyoperationContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterAddoperation(ICSSParser.AddoperationContext ctx) {
		AddOperation addOperation = new AddOperation();
		currentContainer.push(addOperation);
	}

	@Override public void exitAddoperation(ICSSParser.AddoperationContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterSubtractoperation(ICSSParser.SubtractoperationContext ctx) {
		SubtractOperation subtractOperation = new SubtractOperation();
		currentContainer.push(subtractOperation);
	}

	@Override public void exitSubtractoperation(ICSSParser.SubtractoperationContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}


	@Override public void enterIfclause(ICSSParser.IfclauseContext ctx) {
		IfClause ifClause = new IfClause();
		currentContainer.push(ifClause);
	}

	@Override public void exitIfclause(ICSSParser.IfclauseContext ctx) {
		removeAsTopAndAddAsChildToParent();
	}

	@Override public void enterExpression(ICSSParser.ExpressionContext ctx) {
		// seems unnecessary since this gets handled by the bool literal and variable reference
	}

	@Override public void exitExpression(ICSSParser.ExpressionContext ctx) {
		// seems unnecessary since this gets handled by the bool literal and variable reference
		// just here for if i need it later
	}


	//removes current top and then adds it as child to the one above it.
	private void removeAsTopAndAddAsChildToParent(){
		ASTNode node = currentContainer.pop();
		currentContainer.peek().addChild(node);
	}

}
