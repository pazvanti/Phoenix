package tech.petrepopescu.phoenix.special;

public abstract class PhoenixContent {
    protected final StringBuilder contentBuilder = new StringBuilder();
    public abstract String render();

    @Override
    public String toString() {
        return render();
    }
}
