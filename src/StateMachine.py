class StateMachine:
    def __init__(self):
        self._stack = []

    def push(self, state):
        self._stack.append(state)

    def pop(self):
        return self._stack.pop()

    def replace(self, state):  # menu -> game
        self._stack.pop()
        self._stack.append(state)

    @property
    def current(self):
        return self._stack[-1] if self._stack else None
