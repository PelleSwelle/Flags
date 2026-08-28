import pygame_gui
import pygame
from GlobalValues import Global
from API import (
    get_all_country_names,
    get_playable_country_names,
    get_continents,
    get_flags_in_continent,
)
from typing import List


class UI:
    BUTTON_HEIGHT = 50
    pygame.font.init()
    manager = pygame_gui.UIManager((Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT))

    dropdown_size = (200, BUTTON_HEIGHT)

    playable_names = [name for _, name in get_playable_country_names()]
    all_names = [name for _, name in get_all_country_names()]

    all_continents = [name for _, name in get_continents()]

    container = pygame_gui.elements.UIPanel(
        relative_rect=pygame.Rect((0, 0), (200, 800)), manager=manager
    )

    # ************* MENU *************

    title = pygame_gui.elements.UITextBox(
        relative_rect=pygame.Rect((0, 0), (195, 50)),
        html_text="Flag Assembly",
        manager=manager,
        container=container,
    )

    flags_dropdown = pygame_gui.elements.UIDropDownMenu(
        options_list=[(name, str(id)) for id, name in get_playable_country_names()],
        starting_option=(
            get_all_country_names()[0][1],
            str(get_all_country_names()[0][0]),
        ),
        relative_rect=pygame.Rect((0, 0), dropdown_size),
        manager=manager,
    )

    continents_dropdown = pygame_gui.elements.UIDropDownMenu(
        options_list=[(name, str(id)) for id, name in get_continents()],
        starting_option=(get_continents()[0][1], str(get_continents()[0][0])),
        relative_rect=pygame.Rect((20, 20), dropdown_size),
        manager=manager,
    )
    continents_dropdown.hide()

    load_continent_button = pygame_gui.elements.UIButton(
        relative_rect=pygame.Rect((300, 20), (150, BUTTON_HEIGHT)),
        text="load selected continent",
        manager=manager,
    )
    load_continent_button.hide()

    load_button = pygame_gui.elements.UIButton(
        relative_rect=pygame.Rect((300, 100), (150, BUTTON_HEIGHT)),
        text="load selected flag",
        manager=manager,
    )

    # ************* IN GAME *************

    check_button = pygame_gui.elements.UIButton(
        relative_rect=pygame.Rect((300, 175), (150, BUTTON_HEIGHT)),
        text="Check",
        manager=manager,
    )

    information_box = pygame_gui.elements.UITextBox(
        html_text="[Category] in [Region], [Continent]",
        relative_rect=pygame.Rect((400, 200), (200, 200)),
    )

    piece_description_box = pygame_gui.elements.UITextBox(
        html_text="Description of the symbolism of the given piece",
        relative_rect=pygame.Rect((pygame.mouse.get_pos()), (300, 50)),
    )

    next_button = pygame_gui.elements.UIButton(
        relative_rect=pygame.Rect((800, 800), (150, BUTTON_HEIGHT)),
        text="Next Flag",
        manager=manager,
    )

    @classmethod
    def handle_button_event(cls, event, game):
        # TODO: always load flag as part of a range. Also if only playing a single flag
        if event.ui_element == cls.load_button:
            flag_id = int(cls.flags_dropdown.selected_option[1])
            # game.set_current_flag(flag_id)
            game.set_current_range([flag_id])
        if event.ui_element == cls.load_continent_button:
            continent_id = int(cls.continents_dropdown.selected_option[1])
            game.set_current_range(get_flags_in_continent(continent_id))
        elif event.ui_element == cls.check_button:
            if game.current_flag:
                score = game.current_flag.compare()
                print(f"Flag match: {score:.1f}%")
        elif event.ui_element == cls.piece_description_box:
            cls.piece_description_box.show()


def draw_tooltip(surface, text, mouse_pos, max_width=300):
    font = pygame.font.Font(None, 22)
    lines = _wrap_text(text, font, max_width)
    line_height = font.get_linesize()
    tooltip_width = min(max(font.size(l)[0] for l in lines) + 20, max_width + 20)
    tooltip_height = line_height * len(lines) + 16

    tooltip_x = mouse_pos[0] + 15
    tooltip_y = mouse_pos[1] - 10
    if tooltip_x + tooltip_width > Global.SCREEN_WIDTH:
        tooltip_x = mouse_pos[0] - tooltip_width - 15
    if tooltip_y + tooltip_height > Global.SCREEN_HEIGHT:
        tooltip_y = Global.SCREEN_HEIGHT - tooltip_height - 5
    if tooltip_y < 0:
        tooltip_y = 5

    bg = pygame.Surface((tooltip_width, tooltip_height), pygame.SRCALPHA)
    bg.fill((0, 0, 0, 200))
    surface.blit(bg, (tooltip_x, tooltip_y))

    for i, line in enumerate(lines):
        text_surf = font.render(line, True, (255, 255, 255))
        surface.blit(text_surf, (tooltip_x + 10, tooltip_y + 8 + i * line_height))


def _wrap_text(text: str, font, max_width: int) -> List[str]:
    words = text.split(" ")
    lines = []
    current = ""
    for word in words:
        test = f"{current} {word}".strip()
        if font.size(test)[0] > max_width and current:
            lines.append(current)
            current = word
        else:
            current = test
    if current:
        lines.append(current)
    return lines


def draw_hover_tooltip(flag):
    mouse_pos = pygame.mouse.get_pos()
    for piece in reversed(flag.pieces):
        screen_x = flag.assembly_area_rect.x + int(piece.rect.x - flag.min_x)
        screen_y = flag.assembly_area_rect.y + int(piece.rect.y - flag.min_y)
        piece_rect = pygame.Rect(
            screen_x, screen_y, piece.rect.width, piece.rect.height
        )
        if piece_rect.collidepoint(mouse_pos) and piece.meaning:
            draw_tooltip(Global.display_surf, piece.meaning, mouse_pos)
            break
