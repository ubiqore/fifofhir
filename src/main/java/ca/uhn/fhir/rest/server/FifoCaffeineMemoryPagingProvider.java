package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

import org.apache.commons.lang3.Validate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


// for test



public class FifoCaffeineMemoryPagingProvider extends BasePagingProvider implements IPagingProvider {

        // le nouveau cache from caffeine
        private Cache<String, IBundleProvider> cache;
        // cache remplace myBundleProviders
        //  private LinkedHashMap<String, IBundleProvider> myBundleProviders;

        // mySize disparait car caffeine s'en occupe.
        //private int mySize;




        public FifoCaffeineMemoryPagingProvider(Long cacheEntriesMax,
                                                Long weightMax ,
                                                Long expireAfterWriteInMinutes,
                                                boolean useRemovalListener
                                               ) {
                // validate the fact that cacheEntriesMax XOR weightMax should provided (not both)
                Validate.isTrue(cacheEntriesMax == null || weightMax == null, "cacheEntriesMax XOR weightMax should be null");
               // Validate.isTrue(cacheEntriesMax != null || weightMax !=null, "cacheEntriesMax XOR weightMax should be provided");

                // if data provided / check > 0)
                if (expireAfterWriteInMinutes !=null)
                    Validate.isTrue(expireAfterWriteInMinutes.longValue() > 0, "expireAfterWriteInSeconds must be greater than 0");
                if (cacheEntriesMax!=null)
                   Validate.isTrue(cacheEntriesMax.longValue() > 0, "keysNumberMax must be greater than 0");
                if (weightMax !=null)
                    Validate.isTrue(weightMax.longValue() > 0, "weight must be greater than 0");

                Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();

                if (useRemovalListener){
                    cacheBuilder.removalListener((String key, IBundleProvider graph, RemovalCause cause) ->
                            System.out.printf("Key %s was removed (%s)%n", key, cause));
                }
                if (expireAfterWriteInMinutes!=null){
                    cacheBuilder.expireAfterWrite(expireAfterWriteInMinutes.longValue(),TimeUnit.MINUTES);
                }

                if (weightMax !=null){
                    cacheBuilder.maximumWeight(weightMax.longValue()).weigher((String key, IBundleProvider entry) -> entry.size());
                }
                if(cacheEntriesMax !=null){
                    cacheBuilder.maximumSize(cacheEntriesMax.longValue());
                }

                cache = cacheBuilder.build();


        }


        @Override
        public synchronized  IBundleProvider retrieveResultList(String theId) {
                return cache.getIfPresent(theId); // if not present,  return null
        }

        @Override
        public synchronized  String storeResultList(IBundleProvider theList) {

            // automatisé par le cache :
                //while (myBundleProviders.size() > mySize) {
                //        myBundleProviders.remove(myBundleProviders.keySet().iterator().next());
                //}
                String key = UUID.randomUUID().toString();
                cache.asMap().putIfAbsent(key, theList);
              //  cache.put();

                return key;
        }


        // invalidation means manual removal by the caller
        public void invalidate(String theId){
            cache.invalidate(theId);
        }


        public void invalidateAll(){
            cache.invalidateAll();
        }
        public void invalidateSomeOfKeys(Iterable<String> ids){
            cache.invalidateAll(ids);
        }
        public Cache myCache(){
            return cache;
        }



}
