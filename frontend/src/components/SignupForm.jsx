import React, {useEffect, useState} from 'react';
import { Form, Button, Card } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import Signup from "../api/Signup";
import { useNavigate } from "react-router-dom";


const SignupForm = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

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
        console.log('Данные формы:', formData);
        Signup( 'POST', formData)
            .then(data => {
                console.log('Success:', data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
        setRedirect(true);
    };

    return (
        <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
            <Card style={{ width: '30rem', border: '1px solid #007bff' }}>
                <Card.Body>
                    <Card.Title className="text-center">Регистрация</Card.Title>
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
                            Зарегистрироваться
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    );
};

export default SignupForm;