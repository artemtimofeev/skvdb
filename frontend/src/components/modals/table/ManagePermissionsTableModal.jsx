import {Button, Form, Modal} from "react-bootstrap";
import DropdownInputTable from "./DropdownInputTable";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import ManagePermissionsRequest from "../../../api/ManagePermissionsRequest";

function ManagePermissionsTableModal({showPermissions, setShowPermissions, tableName, users}) {
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

    const [username, setUsername] = useState('');
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

    return <Modal show={showPermissions} onHide={() => { setShowPermissions(false)}}>
        <Form onSubmit={handleSubmit}>
            <Modal.Header closeButton>
                <Modal.Title>Manage permissions {tableName}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <DropdownInputTable inputValue={username} setInputValue={setUsername} users={users}/>

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

export default ManagePermissionsTableModal;