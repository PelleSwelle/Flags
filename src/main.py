import pygame
import pygame_gui
from Controller import Controller
from DebugUi import Debug_ui
from ui import UI
from GlobalValues import Global
from Game import Game


class App:
    def __init__(self):
        self._running = True
        self.clock = pygame.time.Clock()
        self.controller = Controller()
        self.game = Game(Global.DATABASE_PATH)
        pygame.font.init()

    def on_init(self):
        pygame.init()
        self.debug_ui = Debug_ui(Global.display_surf)
        self._running = True

    def on_event(self, event):
        UI.manager.process_events(event)
        if event.type == pygame.QUIT:
            self._running = False
        if event.type == pygame_gui.UI_BUTTON_PRESSED:
            UI.handle_button_event(event, self.game)
        if event.type == pygame.KEYUP:
            self.controller.handle_key_event(event, self.game)

    def on_loop(self):
        self.debug_ui.update()
        self.game.update()

    def on_render(self):
        if self.game.current_flag:
            self.game.current_flag.draw_pieces()
        self.debug_ui.draw(Global.display_surf)
        pygame.display.flip()
        Global.display_surf.fill("black")

    def on_cleanup(self):
        pygame.quit()

    def on_execute(self):
        if self.on_init() == False:
            self._running = False

        while self._running:
            time_delta = self.clock.tick(60) / 1000.0
            for event in pygame.event.get():
                self.on_event(event)
            UI.manager.update(time_delta)
            UI.manager.draw_ui(Global.display_surf)
            self.on_loop()
            self.on_render()
        self.on_cleanup()


if __name__ == "__main__":
    theApp = App()
    theApp.on_execute()
