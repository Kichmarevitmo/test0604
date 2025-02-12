package Lab1.Models;

import Lab1.Exceptions.BankException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Этот класс содержит реализацию системы управления банком.
 * Он включает в себя списки, определяющие различные типы банковских карт (кредитные, дебетовые, депозитные),
 * User-ов.
 * Класс Bank также реализует интерфейс IObserver для получения обновлений о состоянии карт.
 * Класс Bank предоставляет методы для добавления пользователей и карт, поиска пользователей и карт по ID,
 * и обновление состояния карты. Он также предоставляет методы для получения списков пользователей и карт,
 * и методы расчета сборов и процентных ставок в зависимости от типа карты и баланса.
 * Этот пакет также включает класс BankException, который используется для индикации ошибок в конструкторе класса Bank.
 */
public class Bank implements IObserver {
    private List<ICard> listCards = new ArrayList<>();
    private List<CreditCard> listCreditCards = new ArrayList<>();
    private List<DebitCard> listDebitCards = new ArrayList<>();
    private List<DepositCard> listDepositCards = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private double firstStepPercent;
    private double secondStepPercent;
    private double thirdStepPercent;
    private double firstStepSum;
    private double secondStepSum;
    private double percentDebitCard;
    private double creditLimit;
    private double commission;
    private String title;
    private double untrustedUserLimit;

    /**
     * Создает новый объект Bank с указанными параметрами.
     *
     * @param title              название банка
     * @param firstStepPercent   процентная ставка для первой ступени шкалы баланса
     * @param secondStepPercent  процентная ставка для второй ступени шкалы баланса
     * @param thirdStepPercent   процентная ставка для третьей ступени шкалы баланса
     * @param firstStepSum       баланс, при котором процентная ставка переходит на вторую ступень
     * @param secondStepSum      баланс, при котором процентная ставка переходит на третью ступень
     * @param percentDebitCard   процентная ставка по дебетовым картам
     * @param creditLimit        максимальный отрицательный баланс по кредитным картам
     * @param commission         ставка комиссии для кредитных карт
     * @param untrustedUserLimit максимальный баланс для неверифицированных пользователей
     * @throws BankException если какое-либо из значений параметра недопустимо
     */
    public Bank(String title, double firstStepPercent, double secondStepPercent, double thirdStepPercent,
                double firstStepSum, double secondStepSum, double percentDebitCard, double creditLimit,
                double commission, double untrustedUserLimit) throws Exception {
        if (title == null || title.trim().isEmpty()) {
            throw new BankException("Incorrect bank name format");
        }
        if (secondStepPercent < firstStepPercent) {
            throw new BankException("The second percentage step is less than the first");
        }
        if (thirdStepPercent < secondStepPercent) {
            throw new BankException("The third percentage step is less than the second");
        }
        if (secondStepSum < firstStepSum) {
            throw new BankException("The second sum money step is less than the first");
        }
        if (creditLimit > 0) {
            throw new BankException("Credit limit must be negative");
        }
        if (commission < 0) {
            throw new BankException("Commission value must be positive");
        }
        if (untrustedUserLimit < 0) {
            throw new BankException("The limit for an unidentified user must be positive");
        }
        this.title = title;
        this.firstStepPercent = firstStepPercent;
        this.secondStepPercent = secondStepPercent;
        this.thirdStepPercent = thirdStepPercent;
        this.firstStepSum = firstStepSum;
        this.secondStepSum = secondStepSum;
        this.percentDebitCard = percentDebitCard;
        this.creditLimit = creditLimit;
        this.commission = commission;
        this.untrustedUserLimit = untrustedUserLimit;
    }

    public String getTitle() {
        return title;
    }

    public List<User> getListUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<ICard> getListCards() {
        return Collections.unmodifiableList(listCards);
    }

    public List<CreditCard> getListCreditCards() {
        return Collections.unmodifiableList(listCreditCards);
    }

    public List<DebitCard> getListDebitCards() {
        return Collections.unmodifiableList(listDebitCards);
    }

    public List<DepositCard> getListDepositCards() {
        return Collections.unmodifiableList(listDepositCards);
    }

    /**
     * Добавляет объект User в список пользователей.
     *
     * @param user Добавляемый объект пользователя
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Ищет объект User в списке пользователей с указанным UUID.
     *
     * @param userId UUID объекта User для поиска
     * @return объект User с указанным UUID или null, если такой User не найден
     */
    public User findUser(UUID userId) {
        return users.stream().filter(u -> u.getUserId() == userId).findFirst().orElse(null);
    }

    /**
     * Добавляет новую кредитную карту в список кредитных карт для указанного пользователя.
     *
     * @param dateTime     дата и время добавления кредитной карты
     * @param startBalance начальный баланс кредитной карты
     * @param userId       UUID пользователя, которому принадлежит кредитная карта
     * @throws Exception если пользователь с указанным UUID не найден
     */
    public void addCreditCard(LocalDateTime dateTime, double startBalance, UUID userId) throws Exception {
        listCreditCards.add(new CreditCard(dateTime, startBalance, findUser(userId).verificationPersonalData()));
        listCards.add(listCreditCards.get(listCreditCards.size() - 1));
        findUser(userId).addCard(listCards.get(listCards.size() - 1).getId());
    }

    /**
     * Добавляет новую дебетовую карту в список дебетовых карт для указанного пользователя.
     *
     * @param dateTime     дата и время добавления дебетовой карты
     * @param startBalance начальный баланс дебетовой карты
     * @param userId       UUID пользователя, которому принадлежит дебетовая карта
     * @throws Exception если пользователь с указанным UUID не найден
     */
    public void addDebitCard(LocalDateTime dateTime, double startBalance, UUID userId) throws Exception {
        listDebitCards.add(new DebitCard(dateTime, startBalance, findUser(userId).verificationPersonalData()));
        listCards.add(listDebitCards.get(listDebitCards.size() - 1));
        findUser(userId).addCard(listCards.get(listCards.size() - 1).getId());
    }

    /**
     * Добавляет новую депозитную карту в список депозитных карт для указанного пользователя.
     *
     * @param dateStart    дата начала действия депозитной карты
     * @param dataEnd      дата окончания депозитной карты
     * @param startBalance начальный баланс депозитной карты
     * @param userId       UUID пользователя, которому принадлежит депозитная карта
     * @throws Exception если пользователь с указанным UUID не найден
     */
    public void addDepositCard(LocalDateTime dateStart, LocalDateTime dataEnd, double startBalance, UUID userId) throws Exception {
        listDepositCards.add(new DepositCard(startBalance, dataEnd, dateStart, findUser(userId).verificationPersonalData()));
        listCards.add(listDepositCards.get(listDepositCards.size() - 1));
        findUser(userId).addCard(listCards.get(listCards.size() - 1).getId());
    }

    /**
     * Находит карту с указанным UUID.
     *
     * @param cardId UUID карты для поиска
     * @return карта с указанным UUID или null, если такая карта не найдена
     */
    public ICard findCard(UUID cardId) {
        return listCards.stream()
                .filter(c -> c.getId().equals(cardId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Обновляет состояние банковской системы на основе текущей отметки времени. Этот метод выполняет следующие действия:
     * Проверяет статус проверки персональных данных для карт каждого пользователя и устанавливает флаг идентификации на любой карте, не прошедшей проверку.
     * Обновляет комиссию, кредитный лимит и лимит недоверенных пользователей для каждой кредитной карты в системе и добавляет день в историю платежей карты.
     * Добавляет день в историю платежей и рассчитывает проценты по каждой дебетовой карте в системе на основе текущей отметки времени.
     * Обновляет лимит ненадежных пользователей и рассчитывает проценты для каждой депозитной карты в системе на основе текущей отметки времени и начального баланса карты.
     *
     * @param timeStamp текущая отметка времени
     * @throws Exception если в процессе обновления возникает ошибка
     */
    public void update(LocalDateTime timeStamp) throws Exception {
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < users.get(i).getListCardId().size(); j++) {
                if (users.get(i).verificationPersonalData() != (findCard(users.get(i).getListCardId().get(j)).getIdentification())) {
                    findCard(users.get(i).getListCardId().get(j)).setIdentificationFlag();
                }
            }
        }

        for (int i = 0; i < listCreditCards.size(); i++) {
            listCreditCards.get(i).setCommission(commission);
            listCreditCards.get(i).setCreditLimit(creditLimit);
            listCreditCards.get(i).setUntrustedUserLimit(untrustedUserLimit);
            listCreditCards.get(i).addDay(timeStamp);
        }

        for (int i = 0; i < listDebitCards.size(); i++) {
            listDebitCards.get(i).addDay(timeStamp);
            listDebitCards.get(i).addPercentSum(percentDebitCard);
            if (timeStamp.getDayOfMonth() == listDebitCards.get(i).getDateCreate().getDayOfMonth()) {
                listDebitCards.get(i).interestCalculation();
            }
            listDebitCards.get(i).setUntrustedUserLimit(untrustedUserLimit);
        }

        for (int i = 0; i < listDepositCards.size(); i++) {
            listDepositCards.get(i).addDay(timeStamp);
            listDepositCards.get(i).setUntrustedUserLimit(untrustedUserLimit);
            if (listDepositCards.get(i).getStartBalance() <= firstStepSum) {
                listDepositCards.get(i).addPercentSum(firstStepPercent);
            }
            if (listDepositCards.get(i).getStartBalance() > firstStepSum && listDepositCards.get(i).getStartBalance() <= secondStepSum) {
                listDepositCards.get(i).addPercentSum(secondStepPercent);
            }
            if (listDepositCards.get(i).getStartBalance() > secondStepSum) {
                listDepositCards.get(i).addPercentSum(thirdStepPercent);
            }
            if (timeStamp.getDayOfMonth() == listDepositCards.get(i).getDateCreate().getDayOfMonth()) {
                listDepositCards.get(i).interestCalculation();
            }
        }
    }
}
