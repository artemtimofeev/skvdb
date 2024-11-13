import { CodeBlock, androidstudio } from 'react-code-blocks';

function MyCoolCodeBlock({ code, language, showLineNumbers }) {
    return (
        <div style={{ fontFamily: "JetBrains Mono"}}>
            <CodeBlock
            text={code}
            language={language}
            showLineNumbers={true}
            theme={androidstudio}
            />
        </div>

    );
}

export default MyCoolCodeBlock;