import React, {useState} from "react";
import {Dropdown, Form, InputGroup} from "react-bootstrap";

function DropdownInputTable({inputValue, setInputValue, users}){
    const [showDropdown, setShowDropdown] = useState(false);
    const options = users.map((user, index) => {return user.name});

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
        setShowDropdown(e.target.value.length > 0);
    };

    const handleSelect = (option) => {
        setInputValue(option);
        setShowDropdown(false);
    };

    return (
        <div>
            <Form.Group>
                <InputGroup>
                    <InputGroup.Text id="basic-addon1">@</InputGroup.Text>
                    <Form.Control
                        value={inputValue}
                        onChange={handleInputChange}
                        placeholder="Username"
                        aria-label="Username"
                        aria-describedby="basic-addon1"
                    />
                </InputGroup>
                {showDropdown && (
                    <Dropdown>
                        <Dropdown.Menu show>
                            {options
                                .filter(option => option.toLowerCase().includes(inputValue.toLowerCase()))
                                .map((option, index) => (
                                    <Dropdown.Item key={index} onClick={() => handleSelect(option)}>
                                        {option}
                                    </Dropdown.Item>
                                ))}
                        </Dropdown.Menu>
                    </Dropdown>
                )}
            </Form.Group>
        </div>
    );
}

export default DropdownInputTable;