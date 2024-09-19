package auto_estacio;



public class Veiculo {

  
    private int id;
    private String placa;
    private String tipoVeiculo;
    private String horarioEntrada;
    private String horarioSaidaPermitida;
    private boolean liberarVeiculo;

    // Construtor
    public Veiculo(String placa, String tipoVeiculo, String horarioEntrada, String horarioSaidaPermitida, boolean liberarVeiculo) {
        this.placa = placa;
        this.tipoVeiculo = tipoVeiculo;
        this.horarioEntrada = horarioEntrada;
        this.horarioSaidaPermitida = horarioSaidaPermitida;
        this.liberarVeiculo = liberarVeiculo;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(String horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public String getHorarioSaidaPermitida() {
        return horarioSaidaPermitida;
    }

    public void setHorarioSaidaPermitida(String horarioSaidaPermitida) {
        this.horarioSaidaPermitida = horarioSaidaPermitida;
    }

    public boolean isLiberarVeiculo() {
        return liberarVeiculo;
    }

    public void setLiberarVeiculo(boolean liberarVeiculo) {
        this.liberarVeiculo = liberarVeiculo;
    }

    
}

    

