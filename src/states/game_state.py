import pygame
import pygame_gui
from Controller import Controller
from DebugUi import Debug_ui
from GlobalValues import Global
from states.state import State
from ui import UI


class GameState(State):
    def __init__(self, game, state_machine):
        self.game = game
        self.state_machine = state_machine
        self.controller = Controller()
        self.debug_ui = Debug_ui(Global.display_surf)

    def on_enter(self):
        UI.flags_dropdown.hide()
        UI.load_button.hide()
        UI.check_button.show()

    def handle_event(self, event):
        UI.manager.process_events(event)
        if event.type == pygame.KEYUP:
            if event.key == pygame.K_ESCAPE:
                from states.pause_state import PauseState

                self.state_machine.push(PauseState(self.game, self.state_machine))
            else:
                self.controller.handle_key_event(event, self.game)
        elif event.type == pygame_gui.UI_BUTTON_PRESSED:
            UI.handle_button_event(event, self.game)

    def update(self, dt):
        self.debug_ui.update()
        self.game.update()

    def render(self, screen):
        if self.game.current_flag:
            self.game.current_flag.draw_pieces()
        self.debug_ui.draw(screen)
