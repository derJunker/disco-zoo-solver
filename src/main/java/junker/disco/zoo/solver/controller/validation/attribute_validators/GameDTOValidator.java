package junker.disco.zoo.solver.controller.validation.attribute_validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import junker.disco.zoo.solver.requests.post_bodies.GameDTO;

public class GameDTOValidator implements ConstraintValidator<ValidGameDTO, GameDTO> {

    @Override
    public boolean isValid(GameDTO gameDTO, ConstraintValidatorContext context) {
        if (gameDTO == null) {
            return true; // Null-Werte werden separat behandelt
        }

        return validateBoard(gameDTO, context);
    }


    private boolean validateBoard(GameDTO gameDTO, ConstraintValidatorContext context) {
        var board = gameDTO.board();
        if (board == null) {
            context.buildConstraintViolationWithTemplate("Board cannot be empty")
                    .addPropertyNode("board")
                    .addConstraintViolation();
            return false;
        } else if (board.length == 0 || board[0].length == 0) {
            context.buildConstraintViolationWithTemplate("Board size must be greater than 0")
                    .addPropertyNode("board")
                    .addConstraintViolation();
            return false;
        } else if (board.length != 5 || board[0].length != 5) {
            context.buildConstraintViolationWithTemplate("Board size must be 5x5")
                    .addPropertyNode("board")
                    .addConstraintViolation();
            return false;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                var tile = board[i][j];
                if (tile == null) {
                    context.buildConstraintViolationWithTemplate("Board cannot contain null values")
                            .addPropertyNode("board")
                            .addConstraintViolation();
                    return false;
                }
            }
        }
        return true;
    }
}
