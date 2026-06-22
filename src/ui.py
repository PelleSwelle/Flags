import pygame_gui
import pygame
from GlobalValues import Global
from API import get_all_country_names


class UI:
    BUTTON_HEIGHT = 50
    pygame.font.init()
    manager = pygame_gui.UIManager((Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT))

    dropdown_size = (200, BUTTON_HEIGHT)

    all_ids_and_names = (
        get_all_country_names()
    )  # returns list[tuple(id: int, popular_name: str)]
    all_names = [name for _, name in all_ids_and_names]

    flags_dropdown = pygame_gui.elements.UIDropDownMenu(
        options_list=[(name, str(id)) for id, name in all_ids_and_names],
        starting_option=(all_ids_and_names[0][1], str(all_ids_and_names[0][0])),
        relative_rect=pygame.Rect((100, 100), dropdown_size),
        manager=manager,
    )

    load_button = pygame_gui.elements.UIButton(
        relative_rect=pygame.Rect((300, 100), (150, BUTTON_HEIGHT)),
        text="load selected flag",
        manager=manager,
    )

    check_button = pygame_gui.elements.UIButton(
        relative_rect=pygame.Rect((300, 175), (150, BUTTON_HEIGHT)),
        text="Check",
        manager=manager,
    )

    @classmethod
    def handle_button_event(cls, event, game):
        if event.ui_element == cls.load_button:
            flag_id = int(cls.flags_dropdown.selected_option[1])
            game.set_current_flag(flag_id)
        elif event.ui_element == cls.check_button:
            if game.current_flag:
                score = game.current_flag.compare()
                print(f"Flag match: {score:.1f}%")
