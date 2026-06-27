from states.state import State
from ui import UI
import pygame
import pygame_gui
from GlobalValues import Global


class FinishedState(State):
    def __init__(self, game, state_machine):
        self.game = game
        self.state_machine = state_machine

    def on_enter(self):
        UI.check_button.hide()
        print("Entered finished state")

    def handle_event(self, event):
        UI.manager.process_events(event)
        if event.type == pygame.KEYUP:
            if event.key == pygame.K_ESCAPE:
                from states.menu_state import MenuState

                self.game.current_flag = None
                self.state_machine.replace(MenuState(self.game, self.state_machine))

    def update(self, dt):
        pass

    def render(self, screen):
        if self.game.current_flag:
            self.game.current_flag.draw_pieces()
            self.game.current_flag.draw_hover_tooltip()
