package io.github.k_gregory.otp.rss_reader.repository;

import io.github.k_gregory.otp.rss_reader.entity.OldDocument;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by grego on 13.05.2017.
 */
public interface DocumentRepository extends CrudRepository<OldDocument, Integer> {
}
