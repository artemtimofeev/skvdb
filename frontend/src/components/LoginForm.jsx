import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import Login from "../api/Login";
import {Alert, Button, Card, Form, Spinner} from "react-bootstrap";

const LoginForm = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

    const [loginTried, setLoginTried] = useState(false);
    const [redirect1, setRedirect] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (redirect1) {
            navigate("/");
        }
    }, [redirect1, navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        setLoginTried(true);
        console.log('Данные формы:', formData);
        Login( 'POST', formData)
            .then(data => {
                console.log('Success:', data);
                setRedirect(true);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    };

    return (
        <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
            <Card style={{ width: '30rem', border: '1px solid #007bff' }}>
                <Card.Body>
                    <Card.Title className="text-center">Вход в аккаунт</Card.Title>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formBasicLogin">
                            <Form.Label>Login</Form.Label>
                            <Form.Control placeholder="Введите логин" required name="username" value={formData.username} onChange={handleChange}/>
                        </Form.Group>

                        <Form.Group controlId="formBasicPassword" className="mt-2">
                            <Form.Label>Пароль</Form.Label>
                            <Form.Control type="password" placeholder="Введите пароль" name="password" value={formData.password} required onChange={handleChange}/>
                        </Form.Group>

                        <Button variant="primary" type="submit" className="w-100 mt-4">
                            {loginTried ? <Spinner animation="border" size="sm"/> : "Войти"}
                        </Button>
                    </Form>
                    {loginTried ? <Alert variant="danger" className="mt-4">
                        This is a alert—check it out!
                    </Alert> : ""}
                </Card.Body>
            </Card>
        </div>
    );
};

export default LoginForm;