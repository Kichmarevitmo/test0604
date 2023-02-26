package Lab1.Console;

import Lab1.Models.Bank;
import Lab1.Models.UserBuilder;
import Lab1.Services.CentralBank;
import Lab1.Services.TimeManager;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {
        TimeManager timeManager = new TimeManager(LocalDateTime.now());
        CentralBank centralBank = new CentralBank();
        boolean flag = true;
        while (flag) {
            System.out.println("1: Добавить пользователя в банк");
            System.out.println("2: Создать банк");
            System.out.println("3: Создать кредитную карточку");
            System.out.println("4: Создать дебитовую карточку");
            System.out.println("5: Создать депозитную карточку");
            System.out.println("6: Добавить день");
            System.out.println("7: Добавить месяц");
            System.out.println("8: Перевести сумму");
            System.out.println("9: Отменить транзакцию");
            System.out.println("10: Снять деньги со счета");
            System.out.println("11: Положить деньги на счет");
            System.out.println("12: Выход");
            Scanner scanner = new Scanner(System.in);
            String chooseOperation = scanner.nextLine();
            switch (chooseOperation) {
                case "1":
                    System.out.println("Введите имя:");
                    String name = scanner.nextLine();
                    System.out.println("Введите фамилию:");
                    String surname = scanner.nextLine();
                    System.out.println("Введите начальный баланс:");
                    String balance = scanner.nextLine();
                    System.out.println("Введите адрес:");
                    String address = scanner.nextLine();
                    System.out.println("Введите паспортные данные:");
                    String passportId = scanner.nextLine();
                    System.out.println("Введите банк:");
                    String nameBanks = scanner.nextLine();
                    centralBank.getBank(nameBanks).addUser(new UserBuilder(name, surname, Double.parseDouble(balance)).withAddress(address).withPassportId(Integer.parseInt(passportId)).build());
                    System.out.println("Пользователь создан и добавлен в банк. Его id: " + centralBank.getBank(nameBanks).getListUsers().get(centralBank.getBank(nameBanks).getListUsers().size() - 1).getUserId().toString());
                    break;
                case "2":
                    System.out.println("Введите название банка:");
                    String title = scanner.nextLine();
                    System.out.println("Введите первый уровень процента:");
                    String firstPercent = scanner.nextLine();
                    System.out.println("Введите второй уровень процента:");
                    String secondPercent = scanner.nextLine();
                    System.out.println("Введите третий уровень процента:");
                    String thirdPercent = scanner.nextLine();
                    System.out.println("Введите первый уровень суммы:");
                    String firstStepSum = scanner.nextLine();
                    System.out.println("Введите второй уровень суммы:");
                    String secondStepSum = scanner.nextLine();
                    System.out.println("Процент на остаток для дебетовой карты:");
                    String percentDebitCard = scanner.nextLine();
                    System.out.println("Кредитный лимит:");
                    String creditLimit = scanner.nextLine();
                    System.out.println("Комиссия:");
                    String commission = scanner.nextLine();
                    System.out.println("Лимит для неидентифицированного пользователя:");
                    String untrustedUserLimit = scanner.nextLine();
                    centralBank.addBank(new Bank(title, Double.parseDouble(firstPercent), Double.parseDouble(secondPercent), Double.parseDouble(thirdPercent), Double.parseDouble(firstStepSum), Double.parseDouble(secondStepSum), Double.parseDouble(percentDebitCard), Double.parseDouble(creditLimit), Double.parseDouble(commission), Double.parseDouble(untrustedUserLimit)));
                    timeManager.addObserver(centralBank.getListBanks().get(centralBank.getListBanks().size() - 1));
                    System.out.println("Банк добавлен.");
                    break;
                case "3":
                    System.out.println("Введите название банка:");
                    String titleBank = scanner.nextLine();
                    System.out.println("Введите начальный баланс:");
                    String startBalanceCreditCard = scanner.nextLine();
                    System.out.println("Введите id пользователя:");
                    String guidUser = scanner.nextLine();
                    centralBank.getBank(titleBank).addCreditCard(timeManager.getTimeStamp(), Double.parseDouble(startBalanceCreditCard), centralBank.getBank(titleBank).findUser(UUID.fromString(guidUser)).getUserId());
                    System.out.println(String.format("Кредитная карта создана. Её id: %s", centralBank.getBank(titleBank).getListCreditCards().get(centralBank.getBank(titleBank).getListCreditCards().size() - 1).getCardId().toString()));
                    break;
                case "4":
                    System.out.println("Введите название банка:");
                    String titleBankDebitCard = scanner.nextLine();
                    System.out.println("Введите начальный баланс:");
                    String startBalanceDebitCard = scanner.nextLine();
                    System.out.println("Введите id пользователя:");
                    String guidUserDebitCard = scanner.nextLine();
                    centralBank.getBank(titleBankDebitCard).addDebitCard(timeManager.getTimeStamp(), Double.parseDouble(startBalanceDebitCard), centralBank.getBank(titleBankDebitCard).findUser(UUID.fromString(guidUserDebitCard)).getUserId());
                    System.out.println(String.format("Дебитовая карта создана. Её id: %s", centralBank.getBank(titleBankDebitCard).getListDebitCards().get(centralBank.getBank(titleBankDebitCard).getListDebitCards().size() - 1).getCardId().toString()));
                    break;
                case "5":
                    System.out.println("Введите название банка:");
                    String titleBankDepositCard = scanner.nextLine();
                    System.out.println("Введите начальный баланс:");
                    String startBalanceDepositCard = scanner.nextLine();
                    System.out.println("Введите id пользователя:");
                    String guidUserDepositCard = scanner.nextLine();
                    System.out.println("Введите день:");
                    String day = scanner.nextLine();
                    System.out.println("Введите месяц:");
                    String month = scanner.nextLine();
                    System.out.println("Введите год:");
                    String year = scanner.nextLine();
                    centralBank.getBank(titleBankDepositCard).addDepositCard(timeManager.getTimeStamp(), LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), 0, 0), Double.parseDouble(startBalanceDepositCard), centralBank.getBank(titleBankDepositCard).findUser(UUID.fromString(guidUserDepositCard)).getUserId());
                    System.out.println("Депозитная карта создана. Её id: " + centralBank.getBank(titleBankDepositCard).getListDepositCards().get(centralBank.getBank(titleBankDepositCard).getListDepositCards().size() - 1).getCardId().toString());
                    break;
                case "6":
                    timeManager.addDay();
                    System.out.println("День добавлен.");
                    break;
                case "7":
                    timeManager.addMonth();
                    System.out.println("Месяц добавлен.");
                    break;
                case "8":
                    System.out.println("Введите с какого счета хотите перевести:");
                    String idFrom = scanner.nextLine();
                    System.out.println("Введите на какой счет хотите перевести:");
                    String idTo = scanner.nextLine();
                    System.out.println("Введите сумму перевода:");
                    String transferMoney = scanner.nextLine();
                    centralBank.transferMoney(
                            Double.parseDouble(transferMoney),
                            centralBank.getCard(UUID.fromString(idFrom)).getId(),
                            centralBank.getCard(UUID.fromString(idTo)).getId());
                    System.out.println("Перевод суммы успешно прошел!");
                    break;
                case "9":
                    System.out.println("Введите с какого счета хотите отменить транзакцию:");
                    String idCard = scanner.nextLine();
                    System.out.println("Введите номер транзакции:");
                    String numberTransaction = scanner.nextLine();
                    centralBank.transactionCancellation(
                            centralBank.getCard(UUID.fromString(idCard)).getId(),
                            Integer.parseInt(numberTransaction));
                    System.out.println("Транзакция отменена!");
                    break;
                case "10":
                    System.out.println("Введите с какого счета хотите снять деньги:");
                    String idCardWithdrawMoney = scanner.nextLine();
                    System.out.println("Введите сумму:");
                    String withdrawMoney = scanner.nextLine();
                    centralBank.getCard(UUID.fromString(idCardWithdrawMoney)).withdrawMoney(Double.parseDouble(withdrawMoney));
                    System.out.println("Сумма успешно снята со счета!");
                    break;
                case "11":
                    System.out.println("Введите на какой счет хотите положить деньги:");
                    String idCardTopUp = scanner.nextLine();
                    System.out.println("Введите сумму:");
                    String topUpMoney = scanner.nextLine();
                    centralBank.getCard(UUID.fromString(idCardTopUp)).topUpCard(Double.parseDouble(topUpMoney));
                    System.out.println("Сумма успешно положена!");
                    break;
                case "12":
                    flag = false;
                    break;
            }
        }
    }
}