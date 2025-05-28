import subprocess
import os


class JavaBytecodeExecutor:
    def __init__(self, output_dir: str = "output"):
        self.classpath = os.path.abspath(output_dir)

    def execute(self, class_names: list, ide_input: str = None):
        command = ["java", "-cp", self.classpath] + class_names
        try:
            result = subprocess.run(
                command,
                input=ide_input,
                capture_output=True,
                text=True,
                check=False,
                encoding="utf-8"
            )
            return result.stdout, result.stderr, result.returncode
        except FileNotFoundError:
            return (
                "",
                "Error: 'java' command not found. Please ensure Java Runtime Environment (JRE) is installed and in PATH.",
                -1,
            )
        except Exception as e:
            return "", f"An unexpected error occurred during execution: {e}", -1