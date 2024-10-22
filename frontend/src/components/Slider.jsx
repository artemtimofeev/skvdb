import React from 'react';
import { Container, Card } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

const ScrollableCard = () => {
    return (
        <Container>
            <Card style={{ height: '500px', overflow: 'scroll', background: "black", color: "rgb(169, 183, 198)"}}>
                <Card.Body>
                    <Card.Text style={{whiteSpace: 'nowrap'}}>
                        {/* Длинный текст для демонстрации прокрутки */}
                        2024-10-21 19:43:46,662 [main] WARN
                        o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean
                        'controllerMappingService' of type [org.skvdb.service.ControllerMappingService] is not eligible
                        for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying).
                        Is this bean getting eagerly injected into a currently created BeanPostProcessor
                        [controllerMappingAnnotationBeanPostProcessor]? Check the corresponding BeanPostProcessor
                        declaration and its dependencies.<br/>
                        2024-10-21 19:43:46,667 [main] WARN
                        o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'storageImpl' of type
                        [org.skvdb.storage.StorageImpl] is not eligible for getting processed by all BeanPostProcessors
                        (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a
                        currently created BeanPostProcessor [superuserControllerAnnotationBeanPostProcessor]? Check the
                        corresponding BeanPostProcessor declaration and its dependencies.<br/>
                        2024-10-21 19:43:46,669 [main] WARN
                        o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'getSecuritySettings'
                        of type [org.skvdb.configuration.settings.SecuritySettings] is not eligible for getting
                        processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean
                        getting eagerly injected into a currently created BeanPostProcessor
                        [superuserControllerAnnotationBeanPostProcessor]? Check the corresponding BeanPostProcessor
                        declaration and its dependencies.<br/>
                        2024-10-21 19:43:46,677 [main] WARN
                        o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'userDao' of type
                        [org.skvdb.service.dao.UserDao] is not eligible for getting processed by all BeanPostProcessors
                        (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a
                        currently created BeanPostProcessor [superuserControllerAnnotationBeanPostProcessor]? Check the
                        corresponding BeanPostProcessor declaration and its dependencies.<br/>
                        2024-10-21 19:43:46,677 [main] WARN
                        o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'userServiceImpl' of
                        type [org.skvdb.service.UserServiceImpl] is not eligible for getting processed by all
                        BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly
                        injected into a currently created BeanPostProcessor
                        [superuserControllerAnnotationBeanPostProcessor]? Check the corresponding BeanPostProcessor
                        declaration and its dependencies.<br/>
                    </Card.Text>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default ScrollableCard;