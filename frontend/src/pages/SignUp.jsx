import  SignupForm from '../components/SignupForm';
import {Container} from "react-bootstrap";
import Header from "../components/Header";

function SignUp() {

    return <>
        <Header/>
            <Container>
                <SignupForm/>
            </Container>
    </>
}

export {SignUp};