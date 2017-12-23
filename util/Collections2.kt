package util

class Collections2

fun <E> MutableList<E>.toImmutableList() = InvisibleImmutableList(this)
fun <E> MutableList<E>.toSafeImmutableList() = SafeImmutableList(this)
fun <E> MutableList<E>.toImmutableCollection() = InvisibleImmutableCollection(this)
fun <E> MutableList<E>.toSafeImmutableCollection() = SafeImmutableCollection(this)

fun <E> MutableSet<E>.toImmutableSet() = InvisibleImmutableSet(this)
fun <E> MutableSet<E>.toSafeImmutableSet() = SafeImmutableSet(this)

fun <K,V> Map<K,V>.toImmutableMap() = ImmutableMap<K,V>( this )


abstract class ImmutableCollection<E>: kotlin.collections.Collection<E>
{
    abstract protected val internalList: MutableList<E>
    override val size: Int
         get() = internalList.size
    override fun contains(element: @UnsafeVariance E): Boolean = internalList.contains(element)
    override fun isEmpty(): Boolean = internalList.isEmpty()
    override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean = internalList.containsAll(elements)
    override public fun iterator(): Iterator<E> = internalList.iterator()
    override fun toString() = internalList.toString()
}

final class InvisibleImmutableCollection<E>(list: MutableList<E>): ImmutableCollection<E>()
{
    override protected val internalList: MutableList<E>
    init
    {
        internalList = list
    }
}

final class SafeImmutableCollection<E>(list: MutableList<E>): ImmutableCollection<E>()
{
    override protected val internalList: MutableList<E>
    init
    {
        internalList = mutableListOf()
        internalList.addAll(list)
    }
}

abstract class ImmutableList<E>: ImmutableCollection<E>(), kotlin.collections.List<E>
{
    override public operator fun get(index: Int): E = internalList.get(index)
    override public fun indexOf(element: @UnsafeVariance E): Int = internalList.indexOf(element)
    override public fun lastIndexOf(element: @UnsafeVariance E): Int = internalList.lastIndexOf(element)
    override public fun listIterator(index: Int): ListIterator<E> = internalList.listIterator(index)
    override public fun listIterator(): ListIterator<E> = internalList.listIterator()
    override public fun subList(fromIndex: Int, toIndex: Int): List<E> = internalList.subList(fromIndex, toIndex)
}

final class InvisibleImmutableList<E>(list: MutableList<E>): ImmutableList<E>()
{
    override protected val internalList: MutableList<E>
    init
    {
        internalList = list
    }
}

final class SafeImmutableList<E>(list: MutableList<E>): ImmutableList<E>()
{
    override protected val internalList: MutableList<E>
    init
    {
        internalList = mutableListOf()
        internalList.addAll(list)
    }
}



abstract class ImmutableSet<E>: kotlin.collections.Collection<E>
{
    abstract protected val internalSet: MutableSet<E>
    override val size: Int
        get() = internalSet.size
    override fun contains(element: @UnsafeVariance E): Boolean = internalSet.contains(element)
    override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean = internalSet.containsAll(elements)
    override fun isEmpty(): Boolean = internalSet.isEmpty()
    override public fun iterator(): Iterator<E> = internalSet.iterator()
    override fun toString() = internalSet.toString()
}

final class InvisibleImmutableSet<E>(set: MutableSet<E>): ImmutableSet<E>()
{
    override protected val internalSet: MutableSet<E>
    init
    {
        internalSet = set
    }
}

final class SafeImmutableSet<E>(set: MutableSet<E>): ImmutableSet<E>()
{
    override protected val internalSet: MutableSet<E>
    init
    {
        internalSet = mutableSetOf()
        internalSet.addAll(set)
    }
}

class ImmutableMap<K,V>( map: Map<K,V> ): kotlin.collections.Map<K,V> by map

public inline fun <T, R : Comparable<R>> Array<out T>.maxIndexBy( selector: (T) -> R ): Int {
    if (isEmpty()) return -1

    var maxValue = selector( this[0] )
    var maxIndex = 0

    for ( i in 1..lastIndex ) {
        val v = selector( this[i] )
        if ( maxValue < v ) {
            maxValue = v
            maxIndex = i
        }
    }

    return maxIndex
}

