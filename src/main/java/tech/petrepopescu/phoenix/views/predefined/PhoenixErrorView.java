package tech.petrepopescu.phoenix.views.predefined;

public class PhoenixErrorView extends PredefinedErrorView {
    private final String errorMessage;
    public PhoenixErrorView(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    String getTitle() {
        return "A exception has occurred";
    }

    @Override
    String getMessage() {
        return "An error has occurred and we could not process your request.";
    }

    @Override
    String getSubMessage() {
        return this.errorMessage;
    }
}
