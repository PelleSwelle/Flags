import pygame
import pygame_gui
from GlobalValues import Global
from states.state import State
from ui import UI


class PauseState(State):
    def __init__(self, game, state_machine):
        self.game = game
        self.state_machine = state_machine
        self.resume_button = None
        self.quit_button = None

    def on_enter(self):
        self.resume_button = pygame_gui.elements.UIButton(
            relative_rect=pygame.Rect((400, 300), (200, 50)),
            text="Resume",
            manager=UI.manager,
        )
        self.quit_button = pygame_gui.elements.UIButton(
            relative_rect=pygame.Rect((400, 370), (200, 50)),
            text="Quit to Menu",
            manager=UI.manager,
        )

    def on_exit(self):
        if self.resume_button:
            self.resume_button.kill()
            self.resume_button = None
        if self.quit_button:
            self.quit_button.kill()
            self.quit_button = None

    def handle_event(self, event):
        UI.manager.process_events(event)
        if event.type == pygame.KEYUP:
            if event.key == pygame.K_ESCAPE:
                self.state_machine.pop()
        elif event.type == pygame_gui.UI_BUTTON_PRESSED:
            if event.ui_element == self.resume_button:
                self.state_machine.pop()
            elif event.ui_element == self.quit_button:
                self.game.current_flag = None
                from states.menu_state import MenuState

                self.state_machine.replace(MenuState(self.game, self.state_machine))

    def update(self, dt):
        pass

    def render(self, screen):
        overlay = pygame.Surface(
            (Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT), pygame.SRCALPHA
        )
        overlay.fill((0, 0, 0, 128))
        screen.blit(overlay, (0, 0))
