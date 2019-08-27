package com.revolutmoneyexchange.controller;

import com.revolutmoneyexchange.exceptions.ObjectMisMatchException;
import com.revolutmoneyexchange.model.BankAccount;
import com.revolutmoneyexchange.service.BankAccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * This class is responsible for CRUD operations of Bank Account object
 */
@Path(AccountsController.BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
public class AccountsController {
    public static final String BASE_URL = "/accounts";
    public static final String ACCOUNT_ID_PATH = "id";

    private final static BankAccountService BANK_ACCOUNT_SERVICE = BankAccountService.getInstance();

    /**
     * @return List of all bank account
     *
     */
    @GET
    public Response getAllBankAccounts() {
        Collection<BankAccount> bankAccounts;

        bankAccounts = BANK_ACCOUNT_SERVICE.getAllBankAccounts();

        if (bankAccounts == null) {
            Response.noContent().build();
        }

        return Response.ok(bankAccounts).build();
    }

    /**
     * @param id The ID of Bank Account
     *
     * @return The Bank Account object associated with the ID.
     */
    @GET
    @Path("{" + ACCOUNT_ID_PATH + "}")
    public Response getBankAccountById(@PathParam(ACCOUNT_ID_PATH) Long id) {
        BankAccount bankAccount;


        bankAccount = BANK_ACCOUNT_SERVICE.getBankAccountById(id);

        if (bankAccount == null) {
            throw new WebApplicationException("The bank account is not exists", Response.Status.NOT_FOUND);
        }

        return Response.ok(bankAccount).build();
    }

    /**
     * Updates the particular Bank Account with the parameters provided.
     *
     * @param bankAccount the Bank Account object
     *
     * @return updated Bank Account object. In general it should be object with the same parameters as provided had
     */
    @PUT
    public Response updateBankAccount(BankAccount bankAccount) throws ObjectMisMatchException {
        BankAccountService.getInstance().updateBankAccount(bankAccount);

        return Response.ok(bankAccount).build();
    }

    /**
     * Creates the Bank Account object with the provided parameters. It doesn't mean if provided object will have
     * an ID specified. This ID will be regenerated and returned in the response object
     *
     * @param bankAccount the Bank Account object to create with parameters specified
     *
     * @return Bank Account object with the ID parameter specified.
     */
    @POST
    public Response createBankAccount(BankAccount bankAccount) throws ObjectMisMatchException {
        BankAccount createdBankAccount;

        createdBankAccount = BANK_ACCOUNT_SERVICE.createBankAccount(bankAccount);

        return Response.ok(createdBankAccount).build();
    }
}
