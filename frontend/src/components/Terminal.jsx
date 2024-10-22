import { ReactTerminal } from "react-terminal";

function Terminal() {
    const commands = {
        whoami: "jackharper",
        cd: (directory) => `changed path to ${directory}`
    };

    return (
        <ReactTerminal
            commands={commands}
        />
    );
}

export default Terminal;