import {Button, Col, Container, Row} from "react-bootstrap";
import Header from "../components/Header";
import ReactMarkdown from 'react-markdown'
import MyCoolCodeBlock from "../components/CodeBlock";
import remarkGfm from 'remark-gfm'

function Home() {
    const text = "ConnectionFactory cf = new ConnectionFactory();\n" +
        "\n" +
        "cf.setHost(\"localhost\");\n" +
        "cf.setPort(4004);\n" +
        "cf.setUsername(\"user\");\n" +
        "cf.setPassword(\"password12\");\n" +
        "\n" +
        "Connection conn = cf.createConnection();\n" +
        "\n" +
        "Connection.Executor executor = conn.createExecutor();\n" +
        "executor.set(\"main\", \"123\", \"rrrasnn\");\n" +
        "executor.set(\"main\", \"12313\", \"rrfherr\");\n" +
        "executor.set(\"main\", \"123335\", \"rrrsfs\");\n" +
        "executor.set(\"main\", \"12321\", \"rrrtgs\");\n" +
        "System.out.println(executor.get(\"main\", \"123\"));\n" +
        "\n" +
        "conn.close();"

    const markdown = `A paragraph with *emphasis* and **strong importance**.

> A block quote with ~strikethrough~ and a URL: https://reactjs.org.

* Lists
* [ ] todo
* [x] done

A table:

| a | b |
| - | - |

1. Клиент-серверная модель с взаимодействием по TCP;
2. Поддержка одновременной работы сервера с большим числом клиентов;
3. Аутентификация по логину и паролю, после первого запроса по токену;
4. Данные хранятся в формате ключ — значение;
5. Доступны операции \`get(String key) -> String value\` и \`set(String key, String value)\`;
6. Данные хранятся в оперативной памяти сервера с записью в хронологическом порядке модифицирующих операций в лог. После перезапуска сервера база восстанавливается из лога, затем файл с логом минимизируется (в полученном файле остаются только конечные операции для каждого ключа).

### Пример подключения со стороны клиента
`
    return <>
        <Header/>
        <Container className="mt-4">
            <ReactMarkdown children={markdown} remarkPlugins={[remarkGfm]}></ReactMarkdown>
            <MyCoolCodeBlock code={text} language={"java"} showLineNumbers={100}></MyCoolCodeBlock>
        </Container>
    </>
}

export { Home };