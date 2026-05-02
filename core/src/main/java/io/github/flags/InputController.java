package io.github.flags;

import com.badlogic.gdx.math.Vector2;

public class InputController {
    public static class toggleDebugCommand implements Command {
        private FlagAssembly flagAssembly;

        public toggleDebugCommand(FlagAssembly flagAssembly) {
            this.flagAssembly = flagAssembly;
        }
        @Override
        public void execute() {
            flagAssembly.setDebugEnabled(!flagAssembly.isDebugEnabled);
        }
    }

    // TODO: implement this
    public static class movePieceCommand implements Command {
        private FlagPiece piece;
        private Vector2 direction;

        public movePieceCommand(FlagPiece piece, Vector2 direction) {
            this.piece = piece;
            this.direction = direction;

            this.piece.setPosition(
                this.piece.getX() + direction.x,
                this.piece.getY() + direction.y
            );
            System.out.println("moving piece with command.");
        }

        @Override
        public void execute() {
            new movePieceCommand(this.piece, this.direction);
        }
    }

    public static class CompareCommand implements Command {
        private Flag flag;

        public CompareCommand(Flag flag) {
            this.flag = flag;
        }

        @Override
        public void execute() {
            flag.compare();
        }
    }

    public static class MoveToIntendedPositionsCommand implements Command {
        private Flag flag;

        public MoveToIntendedPositionsCommand(Flag flag) {
            this.flag = flag;
        }

        @Override
        public void execute() {
            flag.moveToIntendedPosition();
        }
    }
}
