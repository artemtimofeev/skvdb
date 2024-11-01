import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import LoginRequest from "../api/authentication/LoginRequest";
import {Alert, Button, Card, Form, Spinner} from "react-bootstrap";
import SetToken from "../storage/SetToken";

const LoginForm = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

    const [loading, setLoading] = useState(false);
    const [redirect, setRedirect] = useState(false);
    const navigate = useNavigate();
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        if (redirect) {
            navigate("/");
        }
    }, [redirect, navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        setLoading(true);
        console.log('Данные формы:', formData);
        LoginRequest(formData.username, formData.password)
            .then(response => {
                SetToken(response.token);
                console.log('Success:', response);
                setTimeout(() => setRedirect(true), 100);
            })
            .catch(error => {
                console.error('Error:', error);
                setErrorMessage(error.message);
                setLoading(false);
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
                            {loading ? <Spinner animation="border" size="sm"/> : "Войти"}
                        </Button>
                    </Form>
                    {errorMessage !== "" ? <Alert variant="danger" className="mt-4">
                        {errorMessage}
                    </Alert> : ""}
                </Card.Body>
            </Card>
        </div>
    );
};

export default LoginForm;