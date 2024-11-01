import {Component} from "react";

class ErrorBoundary extends Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false, error: null, errorInfo: null };
    }

    static getDerivedStateFromError(error) {
        return { hasError: true, error };
    }

    componentDidCatch(error, errorInfo) {
        console.error("Ошибка:", error, errorInfo);
        this.setState({ errorInfo });
    }

    render() {
        if (this.state.hasError) {
            return (
                <div className="d-flex justify-content-center align-items-center" style={{height: '100vh'}}>
                    <h1>{this.state.error?.message}</h1>
                </div>
            );
        }

        return this.props.children;
    }
}

export default ErrorBoundary;