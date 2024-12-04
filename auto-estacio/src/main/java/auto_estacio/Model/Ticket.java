package auto_estacio.Model;



public class Ticket{

  

    private String placa;
    private String tipoVeiculo;
    private String horarioEntrada;

    // Construtor
    public Ticket(String placa, String tipoVeiculo, String horarioEntrada) {
        this.placa = placa;
        this.tipoVeiculo = tipoVeiculo;
        this.horarioEntrada = horarioEntrada;
    }

    // Getters e Setters
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

    
}

    

