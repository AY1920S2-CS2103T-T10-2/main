package cardibuddy.model.deck;

import static cardibuddy.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import cardibuddy.commons.core.LogsCenter;
import cardibuddy.model.flashcard.Flashcard;
import cardibuddy.model.flashcard.UniqueFlashcardList;
import cardibuddy.model.tag.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Represents a Deck in the cardibuddy application.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Deck {

    // Identity fields
    private final Title title;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private List<Flashcard> flashcards = new ArrayList<>();
    private FilteredList<Flashcard> filteredFlashcards;
    private Statistics statistics = new Statistics();
    private final Logger logger = LogsCenter.getLogger(Deck.class.getName());
    private Predicate predicate;

    /**
     * Every field must be present and not null.
     */
    public Deck(Title title, Set<Tag> tags, List<Flashcard> flashcards) {
        requireAllNonNull(title, tags);
        this.title = title;
        this.tags.addAll(tags);
        this.flashcards.addAll(flashcards);
        this.filteredFlashcards = new FilteredList<>(FXCollections.observableList(flashcards));
        logger.info("Created Deck");
    }

    /**
     * Every field must be present and not null.
     */
    public Deck(Title title, Set<Tag> tags) {
        requireAllNonNull(title, tags);
        this.title = title;
        this.tags.addAll(tags);
        logger.info("Created Deck");
    }

    public Deck() {
        title = new Title("");
        flashcards = new ArrayList<>();
    }

    public Title getTitle() {
        return this.title;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public List<Flashcard> getFlashcards() {
        return Collections.unmodifiableList(flashcards);
    }

    /**
     * Delete card from the flashcards and filteredFlashcards list.
     * @param card to be deleted.
     */
    public void deleteCard(Flashcard card) {
        flashcards.remove(card);
        filteredFlashcards = new FilteredList<>(FXCollections.observableList(flashcards));
        filteredFlashcards.setPredicate(predicate);
    }

    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Adds a Flashcard to a Deck.
     * @param card
     * @return the set of Flashcards from the Deck.
     */
    public List<Flashcard> addFlashcard(Flashcard card) {
        flashcards.add(card);
        return Collections.unmodifiableList(flashcards);
    }

    /**
     * Checks if the Title of the Deck exists.
     * @param otherDeck the deck being compared to.
     * @return true if the Deck already exists and false otherwise.
     */
    public boolean isSameDeck(Deck otherDeck) {
        if (otherDeck == this) {
            return true;
        }

        return otherDeck != null
                && otherDeck.getTitle().equals(getTitle());
    }

    public ObservableList<Flashcard> getFlashcardList() {
        UniqueFlashcardList flashcardList = new UniqueFlashcardList();
        flashcardList.setFlashcards(flashcards);
        return flashcardList.asUnmodifiableObservableList();
    }

    /**
     * Returns an unmodifiable view of the list of {@code Flashcard} backed by the internal list of
     * {@code versionedCardiBuddy}
     */
    public ObservableList<Flashcard> getFilteredFlashcardList() {
        return filteredFlashcards;
    }

    /**
     * Updates the filtered flashcards in a deck.
     * @param predicate SearchCardPredicate or FilterCardPredicate.
     */
    public void updateFilteredFlashcardList(Predicate<Flashcard> predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
        filteredFlashcards.setPredicate(predicate);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(title, flashcards, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle()).append("\nTags: ");

        if (!getTags().isEmpty()) {
            getTags().forEach(builder::append);
        } else {
            builder.append("None");
        }

        builder.append("\nNo. of Cards: ").append(flashcards.size());

        return builder.toString();
    }


}
