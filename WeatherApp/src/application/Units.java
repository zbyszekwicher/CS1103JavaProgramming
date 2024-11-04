package application;

// This enum allows for easy unit conversion
public enum Units {
	METRIC("m"),
	SCIENTIFIC("s"),
	FAHRENHEIT("f");
	
	private final String unitCode;

    Units(String unitCode) {
        this.unitCode = unitCode;
    }

    @Override
    public String toString() {
        return unitCode;
    }
    
    public String getScale() {
        switch (this) {
            case METRIC:
                return "°C";
            case FAHRENHEIT:
                return "°F";
            case SCIENTIFIC:
                return "°K";
            default:
                throw new IllegalArgumentException("Unknown unit");
        }
    }
}
