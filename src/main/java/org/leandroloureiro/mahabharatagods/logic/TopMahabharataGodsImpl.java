package org.leandroloureiro.mahabharatagods.logic;

import org.apache.commons.lang3.StringUtils;
import org.leandroloureiro.mahabharatagods.model.God;
import org.leandroloureiro.mahabharatagods.services.IndianGodService;
import org.leandroloureiro.mahabharatagods.services.IndianGodsService;
import org.leandroloureiro.mahabharatagods.services.MahabharataDataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * {@inheritDoc}
 */
@Service
public class TopMahabharataGodsImpl implements TopMahabharataGods {

    private static final Logger LOG = LoggerFactory.getLogger(TopMahabharataGodsImpl.class);

    private final IndianGodsService indianGodsService;
    private final IndianGodService indianGodService;
    private final MahabharataDataSourceService mahabharataDataSourceService;

    public TopMahabharataGodsImpl(IndianGodsService indianGodsService,
                                  IndianGodService indianGodService,
                                  MahabharataDataSourceService mahabharataDataSourceService) {
        this.indianGodsService = indianGodsService;
        this.indianGodService = indianGodService;
        this.mahabharataDataSourceService = mahabharataDataSourceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<God> getTopMahabharataGods() {

        LOG.info("Downloading Mahabharata Book.");
        var mahabharataContent = mahabharataDataSourceService.getMahabharataBook();

        LOG.info("Fetching gods list.");
        var indianGods = indianGodsService.getGodList();

        return mahabharataContent.thenCombineAsync(indianGods, (mahabharata, gods) -> {

                    LOG.info("Downloaded book with total size: {} bytes.", mahabharata.length());
                    LOG.info("Fetched {} gods.", gods.size());

                    return checkGodsAndCount(gods, mahabharata)
                            .stream()
                            .map(CompletableFuture::join)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .sorted(Comparator.comparingLong(God::hitCount).reversed())
                            .limit(3)
                            .toList();
                }
        ).join();

    }

    private List<CompletableFuture<Optional<God>>> checkGodsAndCount(List<String> gods, String mahabharata) {

        return gods.stream()
                .map(god -> indianGodService.isValidIndianGod(god).handle((r, e) -> Objects.isNull(e) && r.getStatusCode().is2xxSuccessful())
                        .thenCompose(valid -> checkValidAndCount(god, valid, mahabharata)))
                .toList();

    }

    private CompletableFuture<Optional<God>> checkValidAndCount(String god, boolean valid, String mahabharata) {

        return valid ? countAppearances(god, mahabharata) : CompletableFuture.completedFuture(Optional.empty());

    }

    private CompletableFuture<Optional<God>> countAppearances(String god, String mahabharata) {
        return CompletableFuture.supplyAsync(() -> {
            LOG.info("Calculating the appearances for god: {}", god);
            return Optional.of(new God(god, StringUtils.countMatches(mahabharata, god)));
        });
    }

}
