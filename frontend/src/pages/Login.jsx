import Header from "../components/Header";
import {Container} from "react-bootstrap";
import LoginForm from "../components/LoginForm";

function Login() {

    return <>
        <Header/>
        <Container>
            <LoginForm/>
        </Container>
    </>;
}

export { Login };