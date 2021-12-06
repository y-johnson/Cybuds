package com.yjohnson.backend.entities.Interest;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class InterestService {
	private final InterestRepository interestRepository;

	public InterestService(InterestRepository interestRepository) {
		this.interestRepository = interestRepository;
	}

	protected Optional<InterestEntity> getInterestByID(Long id) {
		return interestRepository.findById(id);
	}

	protected Iterable<InterestEntity> getAllInterests() {
		return interestRepository.findAll();
	}

	protected InterestEntity saveUpdatedInterest(InterestEntity valuesToUpdate, InterestEntity interest) {
		return interestRepository.save(interest.updateContents(valuesToUpdate));
	}

	protected Optional<InterestEntity> deleteInterestById(Long id) throws CloneNotSupportedException {
		Optional<InterestEntity> optionalInterest = interestRepository.findById(id);  //1
		if (optionalInterest.isPresent()) {
			InterestEntity deleted = optionalInterest.get().clone();
			interestRepository.delete(optionalInterest.get());                         //2
			return Optional.of(deleted);
		}
		return Optional.empty();
	}

	protected Optional<InterestEntity> getInterestByString(String identifier) {
		Optional<InterestEntity> optionalInterest;
		try {
			// Treat it as a Long first (id)
			Long id = Long.parseLong(identifier);
			optionalInterest = interestRepository.findById(id);     // 1
		} catch (NumberFormatException e) {
			// If it is not a long, treat it as a String (name)
			optionalInterest = interestRepository.findByName(identifier);   // 1
		}
		return optionalInterest;
	}

	protected Optional<InterestEntity> addInterestToDB(InterestEntity newInterestEntity) {
		newInterestEntity.setName(StringUtils.trimWhitespace(StringUtils.capitalize(newInterestEntity.getName())));
		newInterestEntity.setDescription(StringUtils.trimWhitespace(StringUtils.capitalize(newInterestEntity.getDescription())));
		if (interestRepository.findByName(newInterestEntity.getName()).isPresent()) {              //1
			return Optional.empty();
		} else {
			return Optional.of(interestRepository.save(newInterestEntity));
		}
	}
}