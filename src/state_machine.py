from typing import List, Optional
from states.state import State


class StateMachine:
    def __init__(self):
        self._stack: List[State] = []

    def push(self, state: State):
        self._stack.append(state)
        state.on_enter()

    def pop(self):
        exited = self._stack.pop()
        exited.on_exit()
        if self._stack:
            self._stack[-1].on_enter()
        return exited

    def replace(self, state: State):
        if self._stack:
            self._stack[-1].on_exit()
            self._stack.pop()
        self._stack.append(state)
        state.on_enter()

    @property
    def current(self) -> Optional[State]:
        return self._stack[-1] if self._stack else None

    def render_all(self, screen):
        for state in self._stack:
            state.render(screen)
