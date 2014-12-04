/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal;

import org.apache.ignite.*;
import org.apache.ignite.lang.*;
import org.gridgain.grid.cache.*;
import org.gridgain.grid.kernal.processors.cache.*;
import org.gridgain.grid.ggfs.*;
import org.gridgain.grid.kernal.processors.interop.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Extended Grid interface which provides some additional methods required for kernal and Visor.
 */
public interface GridEx extends Ignite, ClusterGroupEx, IgniteCluster {
    /**
     * Gets utility cache.
     *
     * @param keyCls Key class.
     * @param valCls Value class.
     * @return Utility cache.
     */
    public <K extends GridCacheUtilityKey, V> GridCacheProjectionEx<K, V> utilityCache(Class<K> keyCls, Class<V> valCls);

    /**
     * Gets the cache instance for the given name if one is configured or
     * <tt>null</tt> otherwise returning even non-public caches.
     *
     * @param <K> Key type.
     * @param <V> Value type.
     * @param name Cache name.
     * @return Cache instance for given name or <tt>null</tt> if one does not exist.
     */
    @Nullable public <K, V> GridCache<K, V> cachex(@Nullable String name);

    /**
     * Gets default cache instance if one is configured or <tt>null</tt> otherwise returning even non-public caches.
     * The {@link GridCache#name()} method on default instance returns <tt>null</tt>.
     *
     * @param <K> Key type.
     * @param <V> Value type.
     * @return Default cache instance.
     */
    @Nullable public <K, V> GridCache<K, V> cachex();

    /**
     * Gets configured cache instance that satisfy all provided predicates including non-public caches. If no
     * predicates provided - all configured caches will be returned.
     *
     * @param p Predicates. If none provided - all configured caches will be returned.
     * @return Configured cache instances that satisfy all provided predicates.
     */
    public Collection<GridCache<?, ?>> cachesx(@Nullable IgnitePredicate<? super GridCache<?, ?>>... p);

    /**
     * Checks if the event type is user-recordable.
     *
     * @param type Event type to check.
     * @return {@code true} if passed event should be recorded, {@code false} - otherwise.
     */
    public boolean eventUserRecordable(int type);

    /**
     * Checks whether all provided events are user-recordable.
     * <p>
     * Note that this method supports only predefined GridGain events.
     *
     * @param types Event types.
     * @return Whether all events are recordable.
     * @throws IllegalArgumentException If {@code types} contains user event type.
     */
    public boolean allEventsUserRecordable(int[] types);

    /**
     * Gets list of compatible versions.
     *
     * @return Compatible versions.
     */
    public Collection<String> compatibleVersions();

    /**
     * @return Grace period left in minutes if bursting or {@code -1} otherwise.
     */
    public long licenseGracePeriodLeft();

    /**
     * Whether or not remote JMX management is enabled for this node.
     *
     * @return {@code True} if remote JMX management is enabled - {@code false} otherwise.
     */
    public boolean isJmxRemoteEnabled();

    /**
     * Whether or not node restart is enabled.
     *
     * @return {@code True} if restart mode is enabled, {@code false} otherwise.
     */
    public boolean isRestartEnabled();

    /**
     * Whether or not SMTP is configured.
     *
     * @return {@code True} if SMTP is configured - {@code false} otherwise.
     */
    public boolean isSmtpEnabled();

    /**
     * Schedule sending of given email to all configured admin emails.
     */
    IgniteFuture<Boolean> sendAdminEmailAsync(String subj, String body, boolean html);

    /**
     * Get GGFS instance returning null if it doesn't exist.
     *
     * @param name GGFS name.
     * @return GGFS.
     */
    @Nullable public IgniteFs ggfsx(@Nullable String name);

    /**
     * Gets interop processor.
     *
     * @return Interop processor.
     */
    public GridInteropProcessor interop();
}
