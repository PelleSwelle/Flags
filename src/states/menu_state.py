import pygame_gui
from states.state import State
from ui import UI


class MenuState(State):
    def __init__(self, game, state_machine):
        self.game = game
        self.state_machine = state_machine

    def on_enter(self):
        UI.flags_dropdown.show()
        UI.load_button.show()
        UI.check_button.hide()
        UI.piece_description_box.hide()
        print("Entered Menu state")

    def handle_event(self, event):
        UI.manager.process_events(event)
        if event.type == pygame_gui.UI_BUTTON_PRESSED:
            UI.handle_button_event(event, self.game)
            if self.game.current_flag:
                from states.game_state import GameState

                self.state_machine.replace(GameState(self.game, self.state_machine))

    def update(self, dt):
        pass

    def render(self, screen):
        pass
