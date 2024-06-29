import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class Conversor {
    private static final String API_KEY = "5b686e5076b78e81eae2504a";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";

    // Função para obter as taxas de câmbio
    public static JSONObject getExchangeRates() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
            return new JSONObject(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double convertCurrency(double amount, String fromCurrency, String toCurrency, JSONObject exchangeRates) {
        try {
            double fromRate = exchangeRates.getJSONObject("conversion_rates").getDouble(fromCurrency);
            double toRate = exchangeRates.getJSONObject("conversion_rates").getDouble(toCurrency);
            return amount * (toRate / fromRate);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n**************************************************\n");
        System.out.println("Bem-vindo ao AS, o Conversor de Moedas!");
        System.out.println("\n***************************************************\n");
        System.out.println("Nossas moedas disponíveis: USD, EUR, GBP, JPY, BRL, CAD");

        // Obter as taxas de câmbio
        JSONObject exchangeRates = getExchangeRates();
        if (exchangeRates == null) {
            System.out.println("Erro ao obter as taxas de câmbio.");
            return;
        }

        // Entrada do usuário
        System.out.print("Digite a moeda de origem: ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Digite a moeda de destino: ");
        String toCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Digite o valor a ser convertido: ");
        double amount = scanner.nextDouble();

        // Conversão
        double convertedAmount = convertCurrency(amount, fromCurrency, toCurrency, exchangeRates);

        // Saída
        if (convertedAmount != -1) {
            System.out.printf("%.2f %s é igual a %.2f %s\n", amount, fromCurrency, convertedAmount, toCurrency);
        } else {
            System.out.println("Erro na conversão.");
        }

        scanner.close();
    }
}

