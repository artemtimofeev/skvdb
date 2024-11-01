import {Button, Form, Modal} from "react-bootstrap";
import DropdownInputUser from "./DropdownInputUser";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import ManagePermissionsRequest from "../../../api/ManagePermissionsRequest";

function ManagePermissionsUserModal({showPermissions, setShowPermissions, username, tables}){
    const [formData, setFormData] = useState({
        isOwner: false,
        isReader: false,
        isWriter: false
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.checked
        });
    };

    const [tableName, setTableName] = useState('');
    const [redirect, setRedirect] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (redirect) {
            navigate("/");
        }
    }, [redirect, navigate]);

    const {instanceId} = useParams();

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(formData);
        ManagePermissionsRequest(tableName, username, formData.isOwner, formData.isReader, formData.isWriter, instanceId)
            .then(data => {
                console.log('Success:', data);
                setRedirect(true);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    };
    return <Modal show={showPermissions} onHide={() => {
        setShowPermissions(false)
    }}>
        <Modal.Header closeButton>
            <Modal.Title>Manage permissions {username}</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
            <Modal.Body>
                <DropdownInputUser inputValue={tableName} setInputValue={setTableName} tables={tables}/>

                <Form.Group className="mb-3 mt-3" controlId="formBasicCheckbox">
                    <Form.Check name="isOwner" onChange={handleChange} type="checkbox" label="OWNER" />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicCheckbox">
                    <Form.Check name="isReader" onChange={handleChange} type="checkbox" label="READ" />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicCheckbox">
                    <Form.Check name="isWriter" onChange={handleChange} type="checkbox" label="WRITE" />
                </Form.Group>
            </Modal.Body>
            <Modal.Footer>
                <Button type="submit" variant="outline-primary">
                    Save
                </Button>
                <Button variant="outline-secondary" onClick={() => {
                    setShowPermissions(false)
                }}>
                    Cancel
                </Button>
            </Modal.Footer>
        </Form>
    </Modal>
}

export default ManagePermissionsUserModal;