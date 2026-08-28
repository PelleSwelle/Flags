import pygame
import pygame_gui
from state_machine import StateMachine
from states.menu_state import MenuState
from GlobalValues import Global
from Application import Application
from ui import UI


class App:
    def __init__(self):
        self._running = True
        self.clock = pygame.time.Clock()
        self.game = Application(Global.DATABASE_PATH)
        self.state_machine = StateMachine()
        pygame.font.init()

    def on_init(self):
        pygame.init()
        self.state_machine.push(MenuState(self.game, self.state_machine))
        self._running = True

    def on_execute(self):
        if self.on_init() == False:
            self._running = False

        while self._running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self._running = False
                elif self.state_machine.current:
                    self.state_machine.current.handle_event(event)

            dt = self.clock.tick(60) / 1000.0
            UI.manager.update(dt)
            if self.state_machine.current:
                self.state_machine.current.update(dt)
            self.state_machine.render_all(Global.display_surf)
            UI.manager.draw_ui(Global.display_surf)
            pygame.display.flip()
            Global.display_surf.fill("black")

        pygame.quit()


if __name__ == "__main__":
    theApp = App()
    theApp.on_execute()
