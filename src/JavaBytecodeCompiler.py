import os
import subprocess


class JavaBytecodeCompiler:
    def __init__(self, output_dir: str = "output"):
        """
        Initializes the JavaBytecodeCompiler.

        Args:
            output_dir (str): The directory where .java and .class files will be stored.
        """
        self.output_dir = os.path.abspath(output_dir)
        self.compiled_class_files = []
        self.compilation_logs = []

    def _ensure_output_dir_exists(self):
        """Ensures the output directory exists, creating it if necessary."""
        if not os.path.exists(self.output_dir):
            try:
                os.makedirs(self.output_dir)
            except OSError as e:
                log_message = f"Error creating output directory {self.output_dir}: {e}"
                raise PermissionError(log_message) from e

    def compile(self, classes_data: dict):
        """
        Compiles Java source code into Java bytecode (.class files) using javac.

        The input `classes_data` is expected to be a dictionary where keys are
        class names (str) and values are lists of strings, with each string
        representing a line of Java code.

        Args:
            classes_data (dict): A dictionary of class structures to compile.

        Returns:
            tuple[bool, list[str]]: A tuple containing:
                - success (bool): True if all classes compiled without errors, False otherwise.
                - messages (list[str]): Log messages from the compilation process.
        """
        self.compiled_class_files = []
        self.compilation_logs = []

        try:
            self._ensure_output_dir_exists()
        except PermissionError:
            return False, self.compilation_logs

        if not classes_data:
            self.compilation_logs.append("No class data provided to compile.")
            return False, self.compilation_logs

        all_compilation_successful = True

        for class_name, java_code_lines in classes_data.items():
            if not java_code_lines:
                continue

            java_file_name = f"{class_name}.java"
            java_file_path = os.path.join(self.output_dir, java_file_name)

            try:
                with open(java_file_path, "w", encoding="utf-8") as f:
                    f.write("\n".join(java_code_lines))
            except IOError as e:
                all_compilation_successful = False
                continue

            compile_command = ["javac", "-d", ".", java_file_name]

            try:
                process = subprocess.run(
                    compile_command,
                    cwd=self.output_dir,
                    capture_output=True,
                    text=True,
                    check=False,
                    encoding="utf-8",
                )

                if process.returncode == 0:
                    class_file_path = os.path.join(
                        self.output_dir, f"{class_name}.class"
                    )

                    if os.path.exists(class_file_path):
                        self.compiled_class_files.append(class_file_path)
                else:
                    all_compilation_successful = False
                    error_message = f"Error compiling {class_name} (return code: {process.returncode}):\n"
                    if process.stdout:
                        error_message += f"Stdout:\n{process.stdout}\n"
                    if process.stderr:
                        error_message += f"Stderr:\n{process.stderr}\n"
                    self.compilation_logs.append(error_message)

            except FileNotFoundError:
                self.compilation_logs.append(
                    "CRITICAL ERROR: 'javac' command not found. "
                    "Please ensure the Java Development Kit (JDK) is installed and 'javac' is in your system's PATH."
                )
                all_compilation_successful = False
                return False, self.compilation_logs
            except Exception as e:
                self.compilation_logs.append(
                    f"An unexpected error occurred during compilation of {class_name}: {e}"
                )
                all_compilation_successful = False

        if all_compilation_successful and classes_data:
            self.compilation_logs.append(
                "All provided Java sources processed successfully."
            )
        elif not classes_data:
            pass
        else:
            self.compilation_logs.append(
                "Compilation process finished with one or more errors."
            )

        return all_compilation_successful, self.compilation_logs
