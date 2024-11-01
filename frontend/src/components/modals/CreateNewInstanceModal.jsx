import {Alert, Button, Form, Modal} from "react-bootstrap";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import CreateNewInstanceRequest from "../../api/instances/CreateNewInstanceRequest";

function CreateNewInstanceModal({showCreate, setShowCreate}){
    const [formData, setFormData] = useState({
        instanceName: ''
    });

    const [redirect, setRedirect] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (redirect) {
            navigate(0);
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
        CreateNewInstanceRequest( formData.instanceName)
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
                <Modal.Title>Create new instance</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Group className="mb-3" controlId="tableName">
                    <Form.Label>Instance name</Form.Label>
                    <Form.Control required name="instanceName" value={formData.instanceName} onChange={handleChange}/>
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

export default CreateNewInstanceModal;