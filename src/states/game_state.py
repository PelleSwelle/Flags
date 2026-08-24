import pygame
import pygame_gui
from Controller import Controller
from GlobalValues import Global
from states.state import State
from ui import UI
from states.pause_state import PauseState
from states.finished_state import FinishedState


class GameState(State):
    def __init__(self, game, state_machine):
        self.game = game
        self.state_machine = state_machine
        self.controller = Controller()
        self.dragging = False
        self.drag_offset_x = 0
        self.drag_offset_y = 0

    def on_enter(self):
        UI.flags_dropdown.hide()
        UI.load_button.hide()
        UI.load_continent_button.hide()
        UI.continents_dropdown.hide()
        UI.check_button.show()
        UI.information_box.hide()
        print("entered game state")

    def handle_event(self, event):
        UI.manager.process_events(event)
        if event.type == pygame.KEYUP:
            if event.key == pygame.K_ESCAPE:
                self.state_machine.push(PauseState(self.game, self.state_machine))
            else:
                self.controller.handle_key_event(event, self.game)
        elif event.type == pygame_gui.UI_BUTTON_PRESSED:
            UI.handle_button_event(event, self.game)
            if event.ui_element == UI.check_button:
                self.state_machine.push(FinishedState(self.game, self.state_machine))
        elif event.type == pygame.MOUSEBUTTONDOWN and event.button == 1:
            self._on_mouse_down(event)
        elif event.type == pygame.MOUSEMOTION:
            self._on_mouse_motion(event)
        elif event.type == pygame.MOUSEBUTTONUP and event.button == 1:
            self._on_mouse_up(event)

    def _on_mouse_down(self, event):
        flag = self.game.current_flag
        if not flag:
            return
        piece = self.controller._get_piece_at(event.pos, self.game)
        for p in flag.pieces:
            p.is_active = False
        if piece:
            piece.is_active = True
            ax, ay = flag.assembly_area_rect.topleft
            sx = ax + int(piece.rect.x - flag.min_x)
            sy = ay + int(piece.rect.y - flag.min_y)
            self.drag_offset_x = event.pos[0] - sx
            self.drag_offset_y = event.pos[1] - sy
            self.dragging = True

    def _on_mouse_motion(self, event):
        if not self.dragging:
            return
        flag = self.game.current_flag
        if not flag:
            return
        piece = next((p for p in flag.pieces if p.is_active), None)
        if not piece:
            return
        ax, ay = flag.assembly_area_rect.topleft
        piece.rect.x = event.pos[0] - self.drag_offset_x - ax + flag.min_x
        piece.rect.y = event.pos[1] - self.drag_offset_y - ay + flag.min_y

    def _on_mouse_up(self, event):
        self.dragging = False

    def update(self, dt):
        self.game.update()

    def render(self, screen):
        if self.game.current_flag:
            self.game.current_flag.draw_pieces()
