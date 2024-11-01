import {Alert, Button, Form, Modal} from "react-bootstrap";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import CreateNewUserRequest from "../../../api/users/CreateNewUserRequest";

function CreateUserModal({showCreate, setShowCreate}){
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        isSuperuser: false
    });

    const [redirect, setRedirect] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (redirect) {
            navigate("/");
        }
    }, [redirect, navigate]);

    const handleChangeTextField = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleChangeCheckbox = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.checked
        });
    };

    const {instanceId} = useParams();

    const handleSubmit = (e) => {
        e.preventDefault();
        CreateNewUserRequest( formData.username, formData.password, formData.isSuperuser, instanceId)
            .then(data => {
                console.log('Success:', data);
                setRedirect(true);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    };

    return <Modal show={showCreate} onHide={() => {setShowCreate(false)}}>
        <Form onSubmit={handleSubmit}>
            <Modal.Header closeButton>
                <Modal.Title>Create new user</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Username</Form.Label>
                    <Form.Control required name="username" value={formData.username} onChange={handleChangeTextField}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Password</Form.Label>
                    <Form.Control required name="password" value={formData.password} onChange={handleChangeTextField}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicCheckbox">
                    <Form.Check name="isSuperuser" onChange={handleChangeCheckbox} type="checkbox" label="Superuser" />
                </Form.Group>
                <Alert key="danger" variant="danger">
                    Error
                </Alert>
            </Modal.Body>
            <Modal.Footer>
                <Button type="submit" variant="outline-primary">
                    Create
                </Button>
                <Button variant="outline-secondary" onClick={() => {setShowCreate(false)}}>
                    Cancel
                </Button>
            </Modal.Footer>
        </Form>
    </Modal>
}

export default CreateUserModal;