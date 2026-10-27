package com.prestashop.test.pages;

import java.util.Map;

public final class Texts {

    private static final Map<String, Map<String, String>> VALUES = Map.of(
            "en", Map.ofEntries(
                    Map.entry("language", "English"), Map.entry("search", "Search our catalog"),
                    Map.entry("signIn", "Sign in"), Map.entry("signOut", "Sign out"),
                    Map.entry("continue", "Continue shopping"), Map.entry("checkout", "Proceed to checkout"),
                    Map.entry("register", "No account? Create one here"), Map.entry("login", "Log in to your account"),
                    Map.entry("registration", "Create an account"), Map.entry("save", "Save"),
                    Map.entry("account", "Your account"), Map.entry("description", "Description"),
                    Map.entry("details", "Product Details"), Map.entry("size", "Size"),
                    Map.entry("cartEmpty", "There are no more items in your cart"),
                    Map.entry("noMatches", "No matches were found for your search"),
                    Map.entry("searchResults", "Search Results"), Map.entry("brands", "Brands"),
                    Map.entry("continueCheckout", "Continue"), Map.entry("placeOrder", "Place order"),
                    Map.entry("checkoutPersonalInformation", "1 PERSONAL INFORMATION"),
                    Map.entry("checkoutAddresses", "2 ADDRESSES"),
                    Map.entry("checkoutShipping", "3 SHIPPING METHOD"),
                    Map.entry("checkoutPayment", "4 PAYMENT"),
                    Map.entry("confirmation", "Your order is confirmed")),
            "nl", Map.ofEntries(
                    Map.entry("language", "Nederlands"), Map.entry("search", "Doorzoek onze catalogus"),
                    Map.entry("signIn", "Inloggen"), Map.entry("signOut", "Uitloggen"),
                    Map.entry("continue", "Ga door met winkelen"), Map.entry("checkout", "Doorgaan naar betalen"),
                    Map.entry("register", "Geen account? Maak er één aan"), Map.entry("login", "Aanmelden op je account"),
                    Map.entry("registration", "Account aanmaken"), Map.entry("save", "Opslaan"),
                    Map.entry("account", "Uw account"), Map.entry("description", "Omschrijving"),
                    Map.entry("details", "Productdetails"), Map.entry("size", "Grootte"),
                    Map.entry("cartEmpty", "Er zijn geen producten meer in uw wagen"),
                    Map.entry("noMatches", "Geen resultaten gevonden voor uw zoekopdracht"),
                    Map.entry("searchResults", "Zoekresultaten"), Map.entry("brands", "Merken"),
                    Map.entry("continueCheckout", "Doorgaan"), Map.entry("placeOrder", "Plaats bestelling"),
                    Map.entry("checkoutPersonalInformation", "1 Persoonlijke gegevens"),
                    Map.entry("checkoutAddresses", "2 Adressen"),
                    Map.entry("checkoutShipping", "3 Verzendmethode"),
                    Map.entry("checkoutPayment", "4 Betaling"),
                    Map.entry("confirmation", "Uw bestelling is bevestigd")));

    private Texts() {
    }

    public static String get(String locale, String key) {
        Map<String, String> translations = VALUES.get(locale);
        if (translations == null || !translations.containsKey(key)) {
            throw new IllegalArgumentException("Missing translation for " + locale + ": " + key);
        }
        return translations.get(key);
    }
}
