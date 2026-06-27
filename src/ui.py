import pygame_gui
import pygame
from GlobalValues import Global
from API import get_all_country_names, get_playable_country_names

class UI:
    BUTTON_HEIGHT = 50
    pygame.font.init()
    manager = pygame_gui.UIManager((Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT))

    dropdown_size = (200, BUTTON_HEIGHT)


    playable_names = [name for _, name in get_playable_country_names()]
    all_names = [name for _, name in get_all_country_names()]

    # ************* MENU *************
    flags_dropdown = pygame_gui.elements.UIDropDownMenu(
        options_list=[(name, str(id)) for id, name in get_playable_country_names()],
        starting_option=(get_all_country_names()[0][1], str(get_all_country_names()[0][0])),
        relative_rect=pygame.Rect((100, 100), dropdown_size),
        manager=manager,
    )

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
       relative_rect=pygame.Rect((400, 200), (200, 200))
    )

    piece_description_box = pygame_gui.elements.UITextBox(
        html_text="Description of the symbolism of the given piece",
        relative_rect=pygame.Rect((pygame.mouse.get_pos()), (300, 50))
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
        elif event.ui_element == cls.piece_description_box:
            cls.piece_description_box.show()
