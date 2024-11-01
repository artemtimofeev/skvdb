import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import {Button} from "react-bootstrap";
import {Link, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import IsLoggedIn from "../storage/IsLoggedIn";
import DeleteToken from "../storage/DeleteToken";

function Header() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        setIsLoggedIn(IsLoggedIn());
    }, []);

    return (
        <Navbar expand="lg" className="bg-body-tertiary">
            <Container>
                <Navbar.Brand href="#home">Managed skvdb</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav" className="full-width-collapse">
                    <Nav className="me-auto">
                        <Link to="/" style={{ textDecoration: 'none', color: 'inherit' }}><Nav.Link href="/">Getting started</Nav.Link></Link>
                        <Nav.Link href="/documentation">Documentation</Nav.Link>
                        {isLoggedIn && <>
                        <Link to="/instances" style={{ textDecoration: 'none', color: 'inherit' }}>
                            <Nav.Link href="/instances">Instances</Nav.Link>
                        </Link>
                        </>}
                    </Nav>
                    {isLoggedIn && <>
                        <NavDropdown className="ms-auto pt-2 pb-2" title="Account" id="basic-nav-dropdown">
                            <Link to="/billing" style={{ textDecoration: 'none', color: 'inherit' }}>
                                <NavDropdown.Item href="/billing">
                                Billing
                                </NavDropdown.Item>
                            </Link>
                            <NavDropdown.Item href="/action/3.1" disabled="true">Profile</NavDropdown.Item>
                            <NavDropdown.Divider />
                            <NavDropdown.Item href="/" onClick={() => {DeleteToken(); navigate(0);}}>
                                Logout
                            </NavDropdown.Item>
                        </NavDropdown></>}
                </Navbar.Collapse>
                {isLoggedIn && <></>
                    }
                {!isLoggedIn && <><Link to="/signup"><Button variant="dark">Sign up</Button></Link>
                <Link to="/login"><Button variant="primary" className="ms-2">Login</Button></Link></>}
            </Container>
        </Navbar>
    );
}

export default Header;