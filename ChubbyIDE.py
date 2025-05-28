import customtkinter
from tkinter import messagebox, filedialog
import re
from src.ChubbyCompiler import ChubbyCompiler

customtkinter.set_appearance_mode("dark")
customtkinter.set_default_color_theme("blue")


class ChubbyIDE:
    APP_BACKGROUND = "#242424"
    FRAME_COLOR = "#2D2D2D"
    TEXTBOX_COLOR = "#1E1E1E"
    TEXTBOX_TEXT_COLOR = "#DCE4EE"
    BUTTON_COLOR = "#3A7EBF"
    BUTTON_HOVER_COLOR = "#326A9F"
    LOAD_BUTTON_COLOR = "#007ACC"
    LOAD_BUTTON_HOVER_COLOR = "#005C99"
    CLEAR_BUTTON_COLOR = "#555555"
    CLEAR_BUTTON_HOVER_COLOR = "#666666"
    LABEL_TEXT_COLOR = "#DCE4EE"

    SUCCESS_FG = "#39CC8F"
    ERROR_FG = "#FF6B6B"
    INFO_FG = "#5CACEE"
    WARNING_FG = "#FFA500"

    EDITOR_FONT_FAMILY = "JetBrains Mono"
    UI_FONT_FAMILY = "Roboto"
    LOG_FONT_FAMILY = "Consolas"

    EDITOR_FONT_SIZE = 14
    UI_REGULAR_FONT_SIZE = 13
    UI_TITLE_FONT_SIZE = 15
    LOG_FONT_SIZE = 12
    INPUT_AREA_FONT_SIZE = 12

    CORNER_RADIUS = 6

    SYNTAX_TAGS = {
        "comment": {"foreground": "#6A9955"},
        "string_literal": {"foreground": "#CE9178"},
        "number_literal": {"foreground": "#B5CEA8"},
        "keyword_control": {"foreground": "#C586C0"},
        "keyword_type": {"foreground": "#4EC9B0"},
        "keyword_literal": {"foreground": "#569CD6"},
        "keyword_modifier": {"foreground": "#9CDCFE"},
        "keyword_other": {"foreground": "#4FC1FF"},
        "operator_symbol": {"foreground": "#D4D4D4"},
        "operator_word": {"foreground": "#C586C0"},
        "punctuation": {"foreground": "#808080"},
    }

    KEYWORDS_CONTROL = [
        'class', 'endclass', 'function', 'endfunction', 'constructor', 'endconstructor',
        'if', 'elsif', 'else', 'endif', 'for', 'endfor', 'while', 'endwhile',
        'next', 'stop', 'return', 'override', 'then', 'do',
    ]
    KEYWORDS_TYPE = ['void', 'bool', 'char', 'int', 'long', 'double', 'string',
                     'List']
    KEYWORDS_LITERAL = ['null', 'true', 'false', 'this']
    KEYWORDS_MODIFIER = ['public', 'private', 'const', 'static']
    KEYWORDS_OTHER = ['new', 'print', 'input', 'in', 'by']

    OPERATORS_WORD = ['and', 'or', 'not']

    OPERATORS_SYMBOLIC_MULTI = [
        '+=', '-=', '*=', '/=', '%=', '==', '!=', '<=', '>=', '..'
    ]
    OPERATORS_SYMBOLIC_SINGLE = [
        '+', '-', '*', '/', '%', '=', '<', '>'
    ]
    PUNCTUATION_SYMBOLS = [
        '(', ')', '[', ']', '{', '}', ',', ';', '.', ':'
    ]

    def __init__(self, master: customtkinter.CTk):
        self.master = master
        master.title("Chubby IDE")
        master.geometry("1300x900")

        self.highlight_job_id = None

        master.grid_rowconfigure(0, weight=10)
        master.grid_rowconfigure(1, weight=1)
        master.grid_columnconfigure(0, weight=3)
        master.grid_columnconfigure(1, weight=2)

        self.code_panel = customtkinter.CTkFrame(master, corner_radius=self.CORNER_RADIUS, fg_color=self.FRAME_COLOR)
        self.code_panel.grid(row=0, column=0, padx=(20, 10), pady=(20, 10), sticky="nsew")
        self.code_panel.grid_rowconfigure(1, weight=1)
        self.code_panel.grid_columnconfigure(0, weight=1)

        self.code_title = customtkinter.CTkLabel(
            self.code_panel, text="Chubby Code Editor",
            font=customtkinter.CTkFont(family=self.UI_FONT_FAMILY, size=self.UI_TITLE_FONT_SIZE, weight="bold"),
            text_color=self.LABEL_TEXT_COLOR)
        self.code_title.grid(row=0, column=0, padx=15, pady=(10, 5), sticky="nw")

        self.code_editor = customtkinter.CTkTextbox(
            self.code_panel,
            font=customtkinter.CTkFont(family=self.EDITOR_FONT_FAMILY, size=self.EDITOR_FONT_SIZE),
            corner_radius=self.CORNER_RADIUS, fg_color=self.TEXTBOX_COLOR, text_color=self.TEXTBOX_TEXT_COLOR,
            border_width=1, border_color="#333333",
            undo=True
        )
        self.code_editor.grid(row=1, column=0, padx=15, pady=(5, 15), sticky="nsew")

        for tag_name, config in self.SYNTAX_TAGS.items():
            self.code_editor.tag_config(tag_name, foreground=config["foreground"])

        self.code_editor.bind("<KeyRelease>", self.on_key_release_highlight, add="+")
        self.code_editor.bind("<<Paste>>", self.on_key_release_highlight, add="+")
        self.code_editor.bind("<<Cut>>", self.on_key_release_highlight, add="+")
        self.code_editor.bind("<FocusIn>", self.on_key_release_highlight, add="+")

        self.output_log_input_panel = customtkinter.CTkFrame(master, corner_radius=self.CORNER_RADIUS,
                                                             fg_color=self.FRAME_COLOR)
        self.output_log_input_panel.grid(row=0, column=1, padx=(10, 20), pady=(20, 10), sticky="nsew")
        self.output_log_input_panel.grid_columnconfigure(0, weight=1)
        self.output_log_input_panel.grid_rowconfigure(1, weight=3)
        self.output_log_input_panel.grid_rowconfigure(3, weight=1)
        self.output_log_input_panel.grid_rowconfigure(5, weight=3)

        self.output_label = customtkinter.CTkLabel(
            self.output_log_input_panel, text="Program Output:",
            font=customtkinter.CTkFont(family=self.UI_FONT_FAMILY, size=self.UI_TITLE_FONT_SIZE, weight="bold"),
            text_color=self.LABEL_TEXT_COLOR)
        self.output_label.grid(row=0, column=0, padx=15, pady=(10, 0), sticky="nw")
        self.program_output = customtkinter.CTkTextbox(
            self.output_log_input_panel,
            font=customtkinter.CTkFont(family=self.LOG_FONT_FAMILY, size=self.LOG_FONT_SIZE),
            state="disabled", corner_radius=self.CORNER_RADIUS, fg_color=self.TEXTBOX_COLOR,
            text_color=self.TEXTBOX_TEXT_COLOR, border_width=1, border_color="#333333")
        self.program_output.grid(row=1, column=0, padx=15, pady=(5, 10), sticky="nsew")

        self.input_label = customtkinter.CTkLabel(
            self.output_log_input_panel, text="Program Input (one value per line):",
            font=customtkinter.CTkFont(family=self.UI_FONT_FAMILY, size=self.UI_TITLE_FONT_SIZE, weight="bold"),
            text_color=self.LABEL_TEXT_COLOR)
        self.input_label.grid(row=2, column=0, padx=15, pady=(10, 0), sticky="nw")
        self.program_input_area = customtkinter.CTkTextbox(
            self.output_log_input_panel,
            font=customtkinter.CTkFont(family=self.LOG_FONT_FAMILY, size=self.INPUT_AREA_FONT_SIZE),
            state="normal", corner_radius=self.CORNER_RADIUS, fg_color=self.TEXTBOX_COLOR,
            text_color=self.TEXTBOX_TEXT_COLOR, border_width=1, border_color="#333333",
            height=70
        )
        self.program_input_area.grid(row=3, column=0, padx=15, pady=(5, 10), sticky="nsew")

        self.logs_label = customtkinter.CTkLabel(
            self.output_log_input_panel, text="Compilation Logs/Errors:",
            font=customtkinter.CTkFont(family=self.UI_FONT_FAMILY, size=self.UI_TITLE_FONT_SIZE, weight="bold"),
            text_color=self.LABEL_TEXT_COLOR)
        self.logs_label.grid(row=4, column=0, padx=15, pady=(10, 0), sticky="nw")
        self.compilation_logs = customtkinter.CTkTextbox(
            self.output_log_input_panel,
            font=customtkinter.CTkFont(family=self.LOG_FONT_FAMILY, size=self.LOG_FONT_SIZE),
            state="disabled", corner_radius=self.CORNER_RADIUS, fg_color=self.TEXTBOX_COLOR,
            text_color=self.TEXTBOX_TEXT_COLOR, border_width=1, border_color="#333333")
        self.compilation_logs.grid(row=5, column=0, padx=15, pady=(5, 15), sticky="nsew")

        self.button_panel = customtkinter.CTkFrame(master, corner_radius=self.CORNER_RADIUS, fg_color="transparent")
        self.button_panel.grid(row=1, column=0, columnspan=2, padx=20, pady=10, sticky="ew")

        self.button_panel.grid_columnconfigure(0, weight=1)
        self.button_panel.grid_columnconfigure(1, weight=0)
        self.button_panel.grid_columnconfigure(2, weight=0)
        self.button_panel.grid_columnconfigure(3, weight=0)
        self.button_panel.grid_columnconfigure(4, weight=1)

        self.load_file_button = customtkinter.CTkButton(
            self.button_panel, text="Load File", command=self.load_file_to_editor,
            font=customtkinter.CTkFont(family=self.UI_FONT_FAMILY, size=self.UI_REGULAR_FONT_SIZE, weight="bold"),
            corner_radius=self.CORNER_RADIUS, fg_color=self.LOAD_BUTTON_COLOR, hover_color=self.LOAD_BUTTON_HOVER_COLOR)
        self.load_file_button.grid(row=0, column=1, padx=10, pady=10, ipady=5)

        self.compile_run_button = customtkinter.CTkButton(
            self.button_panel, text="Compile & Run", command=self.compile_and_run,
            font=customtkinter.CTkFont(family=self.UI_FONT_FAMILY, size=self.UI_REGULAR_FONT_SIZE, weight="bold"),
            corner_radius=self.CORNER_RADIUS, fg_color=self.BUTTON_COLOR, hover_color=self.BUTTON_HOVER_COLOR)
        self.compile_run_button.grid(row=0, column=2, padx=10, pady=10, ipady=5)

        self.clear_button = customtkinter.CTkButton(
            self.button_panel, text="Clear All", command=self.clear_fields,
            font=customtkinter.CTkFont(family=self.UI_FONT_FAMILY, size=self.UI_REGULAR_FONT_SIZE, weight="bold"),
            corner_radius=self.CORNER_RADIUS, fg_color=self.CLEAR_BUTTON_COLOR,
            hover_color=self.CLEAR_BUTTON_HOVER_COLOR)
        self.clear_button.grid(row=0, column=3, padx=10, pady=10, ipady=5)

        self.compiler = ChubbyCompiler()
        self.master.after(100, self.apply_highlighting)

    def on_key_release_highlight(self, event=None):
        if self.highlight_job_id:
            self.master.after_cancel(self.highlight_job_id)
        self.highlight_job_id = self.master.after(250, self.apply_highlighting)

    def apply_highlighting(self):
        if not self.code_editor.winfo_exists():
            return

        cursor_pos = self.code_editor.index(customtkinter.INSERT)
        scroll_pos_y_tuple = self.code_editor.yview()

        for tag in self.SYNTAX_TAGS.keys():
            self.code_editor.tag_remove(tag, "1.0", "end")

        text_content = self.code_editor.get("1.0", "end-1c")
        if not text_content:
            return

        comment_pattern_str = r'#[^\n]*'
        string_main_pattern_str = r'"(?:\\(?:["\\ntbfrux]|u[0-9a-fA-F]{4})|[^"\\])*?"'
        char_main_pattern_str = r"'(?:\\(?:[nt\\'bfrux]|u[0-9a-fA-F]{4})|[^'\\])'"
        string_literal_combined_pattern_str = f"(?:{string_main_pattern_str}|{char_main_pattern_str})"
        double_num_pattern_str = r'\b(?:[0-9]+\.[0-9]*|\.[0-9]+)(?:[eE][+\-]?[0-9]+)?[dD]?\b|\b[0-9]+(?:[eE][+\-]?[0-9]+[dD]?|[dD])\b'
        long_num_pattern_str = r'\b[0-9]+[lL]\b'
        integer_num_pattern_str = r'\b[0-9]+\b'
        number_literal_combined_pattern_str = f"(?:{double_num_pattern_str}|{long_num_pattern_str}|{integer_num_pattern_str})"
        keyword_control_pattern_str = r"\b(?:" + "|".join(re.escape(kw) for kw in self.KEYWORDS_CONTROL) + r")\b"
        keyword_type_pattern_str = r"\b(?:" + "|".join(re.escape(kw) for kw in self.KEYWORDS_TYPE) + r")\b"
        keyword_literal_pattern_str = r"\b(?:" + "|".join(re.escape(kw) for kw in self.KEYWORDS_LITERAL) + r")\b"
        keyword_modifier_pattern_str = r"\b(?:" + "|".join(re.escape(kw) for kw in self.KEYWORDS_MODIFIER) + r")\b"
        keyword_other_pattern_str = r"\b(?:" + "|".join(re.escape(kw) for kw in self.KEYWORDS_OTHER) + r")\b"
        operator_word_pattern_str = r"\b(?:" + "|".join(re.escape(op) for op in self.OPERATORS_WORD) + r")\b"
        ops_sym_multi_str = "|".join(re.escape(op) for op in self.OPERATORS_SYMBOLIC_MULTI)
        ops_sym_single_str = "|".join(re.escape(op) for op in self.OPERATORS_SYMBOLIC_SINGLE)
        operator_symbol_combined_pattern_str = f"(?:(?:{ops_sym_multi_str})|(?:{ops_sym_single_str}))"
        punct_str = "|".join(re.escape(p) for p in self.PUNCTUATION_SYMBOLS)
        punctuation_combined_pattern_str = f"(?:{punct_str})"

        patterns_for_tok_regex = [
            ('comment', comment_pattern_str),
            ('string_literal', string_literal_combined_pattern_str),
            ('number_literal', number_literal_combined_pattern_str),
            ('keyword_control', keyword_control_pattern_str),
            ('keyword_type', keyword_type_pattern_str),
            ('keyword_literal', keyword_literal_pattern_str),
            ('keyword_modifier', keyword_modifier_pattern_str),
            ('keyword_other', keyword_other_pattern_str),
            ('operator_word', operator_word_pattern_str),
            ('operator_symbol', operator_symbol_combined_pattern_str),
            ('punctuation', punctuation_combined_pattern_str),
        ]
        tok_regex = '|'.join('(?P<%s>%s)' % pair for pair in patterns_for_tok_regex)

        try:
            for mo in re.finditer(tok_regex, text_content, re.MULTILINE):
                kind = mo.lastgroup
                start_char, end_char = mo.span()
                start_index = f"1.0+{start_char}c"
                end_index = f"1.0+{end_char}c"
                if kind and kind in self.SYNTAX_TAGS:
                    self.code_editor.tag_add(kind, start_index, end_index)
        except re.error as e:
            print(f"!!! Regex error during highlighting: {e}")
            return
        except Exception as e:
            print(f"!!! Error during highlighting: {e}")
            return

        if self.code_editor.winfo_exists():
            try:
                self.code_editor.mark_set(customtkinter.INSERT, cursor_pos)
                if scroll_pos_y_tuple:
                    self.code_editor.yview_moveto(scroll_pos_y_tuple[0])
            except Exception:
                pass

    def update_text_widget(self, widget: customtkinter.CTkTextbox, content: str, text_color_override=None):
        if not widget.winfo_exists():
            return
        widget.configure(state="normal")
        widget.delete("1.0", "end")

        if text_color_override:
            tag_name = f"log_color_{text_color_override.replace('#', '').lower()}"
            try:
                widget.tag_config(tag_name, foreground=text_color_override)
            except Exception as e:
                print(f"Warning: Could not configure tag {tag_name} for color {text_color_override}: {e}")
                current_default_text_color = widget.cget("text_color")
                widget.configure(text_color=current_default_text_color)
                widget.insert("end", content)
                widget.configure(state="disabled")
                return

            widget.insert("end", content, tag_name)
        else:
            current_default_text_color = widget.cget("text_color")
            effective_default_color = current_default_text_color if current_default_text_color else self.TEXTBOX_TEXT_COLOR
            widget.configure(text_color=effective_default_color)
            widget.insert("end", content)

        widget.configure(state="disabled")

    def load_file_to_editor(self):
        file_path = filedialog.askopenfilename(
            title="Select Chubby File",
            filetypes=(
                ("Text Files", "*.txt"),
                ("Chubby Files", "*.cbb"),
                ("All Files", "*.*")
            )
        )

        if not file_path:
            return

        try:
            with open(file_path, "r", encoding="utf-8") as file:
                content = file.read()

            self.code_editor.delete("1.0", "end")
            self.code_editor.insert("1.0", content)
            self.apply_highlighting()

            self.update_text_widget(self.compilation_logs, f"Successfully loaded file: {file_path}", self.INFO_FG)

        except FileNotFoundError:
            messagebox.showerror("File Error", f"File not found: {file_path}")
            self.update_text_widget(self.compilation_logs, f"Error: File not found {file_path}", self.ERROR_FG)
        except Exception as e:
            messagebox.showerror("Loading Error", f"An error occurred while loading the file: {e}")
            self.update_text_widget(self.compilation_logs, f"Error loading file: {e}", self.ERROR_FG)

    def compile_and_run(self):
        code = self.code_editor.get("1.0", "end-1c").strip()
        user_input_string = self.program_input_area.get("1.0", "end-1c").strip()

        if not code:
            messagebox.showwarning("No Code", "Please enter Chubby code to compile.")
            self.update_text_widget(self.compilation_logs, "No code entered.", self.WARNING_FG)
            self.update_text_widget(self.program_output, "", self.TEXTBOX_TEXT_COLOR)
            return

        self.update_text_widget(self.program_output, "Processing...", self.INFO_FG)
        self.update_text_widget(self.compilation_logs, "Starting compilation and execution...", self.INFO_FG)
        self.master.update_idletasks()

        success, stdout_output, error_logs = self.compiler.compile(code, ide_input=user_input_string)

        self.update_text_widget(self.program_output, stdout_output if stdout_output else "No output data.",
                                self.TEXTBOX_TEXT_COLOR)
        if success:
            log_message = "Compilation and execution successful."
            if error_logs and error_logs.strip():
                log_message += "\nJava Compilation Logs:\n" + error_logs
            self.update_text_widget(self.compilation_logs, log_message, self.SUCCESS_FG)
        else:
            self.update_text_widget(self.compilation_logs, "Compilation or execution failed.\n" + error_logs,
                                    self.ERROR_FG)

    def clear_fields(self):
        self.code_editor.delete("1.0", "end")
        self.apply_highlighting()
        self.program_input_area.delete("1.0", "end")
        self.update_text_widget(self.program_output, "", self.TEXTBOX_TEXT_COLOR)
        self.update_text_widget(self.compilation_logs, "", self.TEXTBOX_TEXT_COLOR)


if __name__ == "__main__":
    root = customtkinter.CTk()
    app = ChubbyIDE(root)
    root.mainloop()