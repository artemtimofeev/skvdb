import {Alert, Button, Form, Modal} from "react-bootstrap";
import CreateNewTableRequest from "../../../api/tables/CreateNewTableRequest";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";

function CreateTableModal({showCreate, setShowCreate}) {
    const [formData, setFormData] = useState({
        tableName: ''
    });

    const [redirect, setRedirect] = useState(false);
    const navigate = useNavigate();

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

    const {instanceId} = useParams();

    const [errorMessage, setErrorMessage] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(formData);
        CreateNewTableRequest( formData.tableName, instanceId)
            .then(() => {
                setRedirect(true);
            })
            .catch(error => {
                setErrorMessage(error.message)
            });
    };

    return <Modal show={showCreate} onHide={() => {setShowCreate(false)}}>
        <Form onSubmit={handleSubmit}>
            <Modal.Header closeButton>
                <Modal.Title>Create new table</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Group className="mb-3" controlId="tableName">
                    <Form.Label>Table name</Form.Label>
                    <Form.Control required name="tableName" value={formData.tableName} onChange={handleChange}/>
                </Form.Group>
                {errorMessage !== "" ? <Alert key="danger" variant="danger">
                    {errorMessage}
                </Alert> : ""}
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

export default CreateTableModal;