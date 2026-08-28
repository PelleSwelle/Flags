from states.state import State
from ui import UI, draw_hover_tooltip
import pygame
import pygame_gui
from GlobalValues import Global


class FinishedState(State):
    def __init__(self, game, state_machine):
        self.game = game
        self.state_machine = state_machine

    def on_enter(self):
        flag = self.game.current_flag
        if flag:
            info_x = flag.assembly_area_rect.right + 15
            info_y = flag.assembly_area_rect.top
            info_width = 280
            info_height = 300
            if info_x + info_width > Global.SCREEN_WIDTH:
                info_x = Global.SCREEN_WIDTH - info_width - 10
            UI.information_box.set_position((info_x, info_y))
            UI.information_box.set_dimensions((info_width, info_height))
            text = (
                f"<b>Name:</b> {flag.popular_name}<br>"
                f"<b>Official Name:</b> {flag.official_state_name}<br>"
                f"<b>Endonym:</b> {flag.endonym}<br>"
                f"<b>Capital:</b> {flag.capital_city}<br>"
                f"<b>Category:</b> {flag.category}<br>"
                f"<b>Region:</b> {flag.region}<br>"
                f"<b>Continent:</b> {flag.continent}<br>"
                f"<b>Score:</b> {flag.score:.1f}%"
            )
            UI.information_box.set_text(text)
            UI.information_box.show()
        UI.check_button.hide()
        UI.next_button.show()
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
            draw_hover_tooltip(self.game.current_flag)
